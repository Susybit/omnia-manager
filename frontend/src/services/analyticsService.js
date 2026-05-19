import api from '@/plugins/axios'

/**
 * Obtiene todas las métricas de analítica del sistema.
 * Los cálculos se realizan en el servidor; aquí solo trasladamos la respuesta.
 *
 * @returns {Promise<Object>} DTO con employees, projects, locationBreakdown y projectLoad.
 */
export async function fetchAnalytics() {
  const res = await api.get('/analytics')
  return res.data
}
