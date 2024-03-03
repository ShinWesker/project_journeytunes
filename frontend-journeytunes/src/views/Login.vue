<template>
  <div>
    <journey-tunes-title class="pt-10">
    </journey-tunes-title>

    <div class="text-center">
      <v-sheet class="pt-24">
        <v-card class="mx-auto pb-2 pt-2 pl-4 pr-4" max-height="300"
                elevation="8"
                max-width="250">
          <v-form
            v-model="form"
            @submit.prevent="onSubmit">

            <div class="d-flex flex-column align-center pt-2">
              <v-text-field
                v-model="username"
                :readonly="loading"
                prepend-inner-icon="mdi-account"
                :rules="[required]"
                label="Username"
                class="mb-4"
                autocomplete="username"
              ></v-text-field>

              <v-text-field
                v-model="password"
                :readonly="loading"
                :rules="[required]"
                type="password"
                prepend-inner-icon="mdi-lock"
                label="Password"
                placeholder="Enter your password"
                class="mb-4"
                autocomplete="current-password"
              ></v-text-field>
            </div>



            <div class="pt-4 pb-2">
              <v-btn
                :disabled="!form"
                :loading="loading"
                block
                color="indigo"
                small
                type="submit"
                variant="elevated"
              >
                Sign In
              </v-btn>

              <v-btn
                :loading="loading"
                block
                color="indigo"
                small
                variant="elevated"
                @click="openRegistration"
                class="mt-2"
              >Register
              </v-btn>
            </div>

          </v-form>
        </v-card>
      </v-sheet>
    </div>
  </div>
</template>

<style scoped>

.v-card{
  border-radius: 1em;
  border: solid #943bd2;
}

.v-text-field {
  width: 95%;
  height: 50px;
  color: indigo;
  background-color: white;
  padding-bottom: 4em;
}

.v-text-field :deep(input) {

  font-size: 0.8em;
  color: black;
}

</style>



<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router';
import store from "@/store";
import axiosClient from "@/clients/axiosClient";
import {useSnackbar} from "@/components/useSnackbar";
import JourneyTunesTitle from "@/components/JourneyTunesTitle.vue";

const registration = ref(false)
const { showSnackbar } = useSnackbar();

const form = ref(false)
const required = (v) => !!v || 'Field is required'
const username = ref(null)
const password = ref(null)
const loading = ref(false)
const errorMessage = ref('')
const router = useRouter();


function openRegistration(){
  router.push('/register')
}

function openLogin(){
  registration.value = false
}

async function onSubmit() {
  if (!form.value) return

  loading.value = true
  errorMessage.value = ''

  try {
    const response = await axiosClient.post('users/api/v1/login', {
      name: username.value,
      password: password.value
    }, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
    showSnackbar("Successfully logged in!", 4000)
    await store.dispatch('logIn', {token: response.data});
    await router.push({name: 'Home'});
  } catch (error) {
    if (error.response && error.response.status === 403) {
      showSnackbar("Wrong username or password", 2500)
    } else {
      showSnackbar("Wrong username or password", 2000)
    }
    console.error('Login error:', error);
  } finally {
    loading.value = false
  }
}
</script>


