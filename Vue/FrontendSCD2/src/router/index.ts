import { createRouter, createWebHistory } from 'vue-router';
import LoginPage from '../components/LoginPage.vue';
import MainPage from '../components/MainPage.vue';
import PendingPostPage from '../components/PendingPostPage.vue';
import RegisterPage from '../components/RegisterPage.vue';
import UserPostsPage from '../components/UserPostsPage.vue';

const routes = [
  { path: '/', component: LoginPage },
  { path: '/main', component: MainPage },
  { path: '/pending', component: PendingPostPage },
  { path: '/register', component: RegisterPage },
  { path: '/userposts', component: UserPostsPage }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;