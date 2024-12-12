import { createRouter, createWebHistory } from 'vue-router';
import LoginPage from '../components/LoginPage.vue';
import MainPage from '../components/MainPage.vue';
import PendingPostPage from '../components/PendingPostPage.vue';
import RegisterPage from '../components/RegisterPage.vue';


const routes = [
  { path: '/', component: LoginPage },
  { path: '/main', component: MainPage },
  { path: '/pending', component: PendingPostPage },
  { path: '/register', component: RegisterPage }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;