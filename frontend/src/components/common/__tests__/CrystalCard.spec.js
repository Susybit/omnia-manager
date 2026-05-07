import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import CrystalCard from '../CrystalCard.vue'

describe('CrystalCard.vue', () => {
  it('renderiza el contenido del slot correctamente', () => {
    const wrapper = mount(CrystalCard, {
      slots: {
        default: '<div class="test-content">Hola Mundo</div>'
      }
    })
    expect(wrapper.find('.test-content').exists()).toBe(true)
    expect(wrapper.text()).toContain('Hola Mundo')
  })

  it('aplica la clase customClass si se proporciona', () => {
    const wrapper = mount(CrystalCard, {
      props: {
        customClass: 'my-custom-style'
      }
    })
    expect(wrapper.classes()).toContain('my-custom-style')
  })

  it('no aplica la clase is-hoverable si hoverable es false', () => {
    const wrapper = mount(CrystalCard, {
      props: {
        hoverable: false
      }
    })
    expect(wrapper.classes()).not.toContain('is-hoverable')
  })

  it('usa el tag especificado en la prop tag', () => {
    const wrapper = mount(CrystalCard, {
      props: {
        tag: 'section'
      }
    })
    expect(wrapper.element.tagName).toBe('SECTION')
  })
})
