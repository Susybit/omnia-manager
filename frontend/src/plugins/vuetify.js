import '@mdi/font/css/materialdesignicons.css'
import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'

/**
 * Configuración global del tema visual (Design System).
 * Centralizamos los colores corporativos para mantener consistencia
 * sin tener que hardcodear valores hexadecimales en las vistas.
 */
const corporateTheme = {
  dark: false,
  colors: {
    background: '#E8F2FF', // Nota: main.css lo sobreescribe a #F4F4F5
    surface: '#FFFFFF',
    primary: '#312E81',
    secondary: '#4F46E5',
    accent: '#1E1B4B',
    'emphasis-soft': '#A5B4FC',
    error: '#EF4444',
    success: '#22C55E',
    warning: '#F59E0B',
    'on-background': '#1A1A1A',
    'on-surface': '#1A1A1A'
  }
}

import { es } from 'vuetify/locale'

export default createVuetify({
  components,
  directives,
  locale: {
    locale: 'es',
    fallback: 'en',
    messages: { es }
  },
  theme: {
    defaultTheme: 'corporateTheme',
    themes: {
      corporateTheme
    }
  },
  defaults: {
    // Valores por defecto globales para erradicar el estilo genérico plano
    VBtn: {
      elevation: 0,
      rounded: 'lg'
    },
    VCard: {
      elevation: 0,
      rounded: 'xl'
    }
  }
})