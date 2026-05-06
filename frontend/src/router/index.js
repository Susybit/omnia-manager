import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '@/layouts/MainLayout.vue'
import { useAuthStore } from '@/stores/auth'

const routes = [
  {
    path: '/login',
    name: 'login',
    component: () => import('@/views/auth/LoginView.vue'),
    meta: { title: 'Acceso', requiresAuth: false }
  },
  {
    path: '/',
    component: MainLayout,
    redirect: '/dashboard',
    meta: { requiresAuth: true },
    children: [
      {
        path: 'dashboard',
        name: 'dashboard',
        component: () => import('@/views/dashboard/DashboardView.vue'),
        meta: { title: 'Panel de Control' }
      },
      {
        path: 'employees',
        name: 'employees',
        component: () => import('@/views/employees/EmployeesView.vue'),
        meta: { title: 'Gestión de Empleados' }
      },
      {
        path: 'employees/new',
        name: 'employee-new',
        component: () => import('@/views/employees/EmployeeFormView.vue'),
        meta: { title: 'Nuevo Empleado' }
      },
      {
        path: 'employees/edit/:id',
        name: 'employee-edit',
        component: () => import('@/views/employees/EmployeeFormView.vue'),
        meta: { title: 'Editar Empleado' }
      },
      {
        path: 'projects',
        name: 'projects',
        component: () => import('@/views/projects/ProjectsView.vue'),
        meta: { title: 'Gestión de Proyectos' }
      },
      {
        path: 'projects/new',
        name: 'project-new',
        component: () => import('@/views/projects/ProjectFormView.vue'),
        meta: { title: 'Nuevo Proyecto' }
      },
      {
        path: 'projects/edit/:id',
        name: 'project-edit',
        component: () => import('@/views/projects/ProjectFormView.vue'),
        meta: { title: 'Editar Proyecto' }
      },
      {
        path: 'assignments',
        name: 'assignments',
        component: () => import('@/views/assignments/AssignmentsView.vue'),
        meta: { title: 'Asignación de Recursos' }
      },
      {
        path: 'profile',
        name: 'profile',
        component: () => import('@/views/profile/ProfileView.vue'),
        meta: { title: 'Mi Perfil' }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/dashboard'
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

// El store debe instanciarse dentro del guard, no en el módulo raíz — Pinia no está listo hasta aquí
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()

  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    next({ name: 'login' })
  } else if (to.name === 'login' && authStore.isAuthenticated) {
    next({ name: 'dashboard' })
  } else {
    next()
  }
})

router.afterEach((to) => {
  document.title = to.meta.title
    ? `${to.meta.title} | Manager`
    : 'Manager | Future Space';
})

export default router