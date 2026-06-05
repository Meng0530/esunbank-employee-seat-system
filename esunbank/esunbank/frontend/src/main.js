import { createApp, h } from 'vue'
import axios from 'axios'
import './style.css'

const api = axios.create({ baseURL: 'http://localhost:8080/api' })

createApp({
  data() {
    return {
      seats: [],
      employees: [],
      selectedEmpId: '',
      selectedSeatSeq: null,
      message: ''
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      const [seatRes, empRes] = await Promise.all([
        api.get('/seats'),
        api.get('/employees')
      ])
      this.seats = seatRes.data
      this.employees = empRes.data
    },
    seatClass(seat) {
      if (this.selectedSeatSeq === seat.floorSeatSeq) return 'seat selected'
      if (seat.empId) return 'seat occupied'
      return 'seat empty'
    },
    selectSeat(seat) {
      if (!this.selectedEmpId) {
        this.message = '請先選擇員工'
        return
      }
      if (seat.empId && seat.empId !== this.selectedEmpId) {
        this.message = '此座位已被佔用，請選擇空位'
        return
      }
      this.selectedSeatSeq = seat.floorSeatSeq
      this.message = ''
    },
    async submit() {
      if (!this.selectedEmpId || !this.selectedSeatSeq) {
        this.message = '請選擇員工與座位'
        return
      }
      try {
        const res = await api.post('/seats/change', {
          empId: this.selectedEmpId,
          newSeatSeq: this.selectedSeatSeq
        })
        this.message = res.data.message
        this.selectedSeatSeq = null
        await this.loadData()
      } catch (e) {
        this.message = e.response?.data?.message || '系統錯誤'
      }
    }
  },
  render() {
    return h('main', [
      h('h2', '員工座位系統'),

      h('section', { class: 'toolbar' }, [
        h('label', '選擇員工：'),
        h('select', {
          value: this.selectedEmpId,
          onChange: e => {
            this.selectedEmpId = e.target.value
          }
        }, [
          h('option', { value: '' }, '請選擇'),
          ...this.employees.map(emp =>
            h('option', { value: emp.empId }, `${emp.empId} - ${emp.name}`)
          )
        ])
      ]),

      h('section', { class: 'seat-grid' },
        this.seats.map(seat =>
          h('button', {
            key: seat.floorSeatSeq,
            class: this.seatClass(seat),
            onClick: () => this.selectSeat(seat)
          }, `${seat.floorNo}樓: 座位${seat.seatNo}${seat.empId ? ` [員編:${seat.empId}]` : ''}`)
        )
      ),

      h('section', { class: 'legend' }, [
        h('span', [h('i', { class: 'box empty-box' }), '空位']),
        h('span', [h('i', { class: 'box occupied-box' }), '已佔用']),
        h('span', [h('i', { class: 'box selected-box' }), '請選擇'])
      ]),

      h('button', { class: 'submit', onClick: this.submit }, '送出'),
      h('p', { class: 'msg' }, this.message)
    ])
  }
}).mount('#app')