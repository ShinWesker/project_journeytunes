
<template>
  <v-layout class="rounded-md">
    <router-link to="/">
      <v-app-bar title="JourneyTunes">
        <button class="p-8" v-if="!isLoggedIn" @click="goToLogin">Login</button>
        <button class="p-8" v-else @click="logOut">Logout</button>




      </v-app-bar>
    </router-link>


    <v-navigation-drawer
      expand-on-hover
      rail
      rail-width="73"
      width="300"
    >
      <v-list>
        <router-link to="/profile">
          <v-list-item class=" pt-4 font-bold pb-4 text-2xl text-no-wrap" prepend-icon="mdi-account">
            Profile
        </v-list-item>
        </router-link>

      </v-list>
      <v-divider></v-divider>
      <v-list density="compact" nav>

          <router-link  to="/plan-your-trip">
            <v-list-item class=" pb-4 text-2xl text-no-wrap" prepend-icon="mdi-map-outline">
              Plan Your Trip</v-list-item>
          </router-link>

        <router-link  to="/create/hotel">
          <v-list-item class="pb-4 text-2xl text-no-wrap" prepend-icon="mdi-home-group-plus">
            Register a Hotel
          </v-list-item>
        </router-link>

      </v-list>
    </v-navigation-drawer>

    <v-main>
      <router-view class="p-5">

      </router-view>
        <GlobalSnackbar>
        </GlobalSnackbar>
    </v-main>
  </v-layout>
</template>

<script setup>
import { computed } from 'vue';
import { useStore } from 'vuex';
import { useRouter } from 'vue-router';
import GlobalSnackbar from "@/components/GlobalSnackbar.vue";

const store = useStore();
const router = useRouter();

const isLoggedIn = computed(() => store.getters['isLoggedIn']);

function goToLogin() {
  router.push({name: 'Login'});
}

function logOut() {
  store.dispatch('logOut').then(() => {
    goToLogin();
  });
  router.push('/login');
}
</script>

<style scoped>

</style>
