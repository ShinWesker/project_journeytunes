import { createRouter, createWebHashHistory } from 'vue-router';
import Home from "@/views/Home.vue";
import Planning from "@/components/Planning.vue";
import HotelDetails from "@/components/HotelDetails.vue";
import Playlists from "@/components/Playlists.vue";
import SpotifySelection from "@/components/SpotifySelection.vue";
import Login from "@/views/Login.vue";
import PlanYourTrip from "@/views/PlanYourTrip.vue";
import {useSnackbar} from "@/components/useSnackbar";
import Register from "@/views/Register.vue";
import CreateHotel from "@/views/CreateHotel.vue";
import Profile from "@/views/Profile.vue";
import Route from "@/components/Route.vue";

const routes = [
  {
    path:"/route",
    name:"Route",
    component: Route
  },
  {
    path:"/plan-your-trip",
    name:"PlanYourTrip",
    component: PlanYourTrip
  },
  {
    path:"/login",
    name:"Login",
    component: Login
  },
  {
    path:"/register",
    name: "Register",
    component: Register
  },
  {
    path: "/",
    name: 'Home',
    component: Home
  },
  {
    path:'/create/hotel',
    name:'CreateHotel',
    component: CreateHotel
  },
  {
    path:'/profile',
    name: 'Profile',
    component: Profile
  }
];

const router = createRouter({
  history: createWebHashHistory(),
  routes
});

const { showSnackbar } = useSnackbar();

router.beforeEach((to, from, next) => {
  const isAuthenticated = localStorage.getItem('authToken');

  const publicPages = ['Login', 'Register'];

  const isPublicPage = publicPages.includes(to.name);

  if (!isPublicPage && !isAuthenticated) {
    showSnackbar("Please login!", 2000);
    next({ name: 'Login' });
  } else if ((to.name === 'Login' || to.name === 'Register') && isAuthenticated) {
    next({ name: 'home' });
  } else {
    next();
  }
});


export default router;
