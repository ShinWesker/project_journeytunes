import { createStore } from 'vuex';
import axiosHotelClient from '@/clients/axiosHotelClient.js';
import {ref} from "vue";


const states = {
  searchedHotels: [],
  requestHotels: []
}

const mutations = {
  getHotels(state, hotels) {
    state.getHotels = hotels;
  },
  setLoggedIn(state, value) {
      state.isLoggedIn = value;
    }
};

const actions = {
  initializeAuthState({ commit }) {
    const token = localStorage.getItem('authToken');
    if (token) {
      commit('setLoggedIn', true);
    } else {
      commit('setLoggedIn', false);
    }
  },
  logIn({ commit }, { token }) {
    localStorage.setItem('userId',token.userId);
    localStorage.setItem('authToken', token.value);

    commit('setLoggedIn', true);
  },
  logOut({ commit }) {
    localStorage.clear();
    commit('setLoggedIn', false);
  },

  getHotels({ commit }) {
    axiosHotelClient.get("/hotels?limit=20").then(({ data }) => {
      commit('getHotels', data);
      console.log(data)
    }).catch(error => console.error("There was an error fetching the hotels:", error));
  },
  requestHotels({ commit }, filters) {
    const queryParams = new URLSearchParams({
      region: filters.region,
      pricePerNight: filters.pricePerNight,
      stars: filters.stars,
      name: filters.name
    }).toString();
    axiosHotelClient.get(`search?${queryParams}`)
      .then(({ data }) => {
        commit('getHotels', data)
      })
      .catch(error => {
        console.error("Failed to fetch hotels:", error);
      });
  }

};

const getters = {
  searchedHotels: state => state.searchedHotels,
  requestsHotels: state => state.requestHotels,
  isLoggedIn: state => state.isLoggedIn
};

const store = createStore({
  states,
  mutations,
  actions,
  getters
});

export const isLoggedIn = ref(false);

export default store;
