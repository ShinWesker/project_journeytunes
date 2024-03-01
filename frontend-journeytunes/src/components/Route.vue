<script setup>
import { onMounted, ref } from "vue";
import L from "leaflet";
import "leaflet/dist/leaflet.css";
import axiosClient from "@/clients/axiosClient";

const mapContainer = ref(null);

async function createRoute(startLat, startLng, endLat, endLng) {
  try {
    const response = await axiosClient().post('routes/api/v1/create', {
      start: {
        lat: startLat,
        lng: startLng
      },
      end: {
        lat: endLat,
        lng: endLng
      }
    });
    return response.data;
  } catch (error) {
    console.error("Error fetching the route:", error);
    return null;
  }
}

function createMap(map, result) {
  if (!result || !map) return;

  let waypoints = result.waypoints.map(wp => L.latLng(wp.lat, wp.lng));

  L.Routing.control({
    waypoints: waypoints,
    routeWhileDragging: false,
  }).addTo(map);

  map.fitBounds([
    [result.start.lat, result.start.lng],
    [result.end.lat, result.end.lng]
  ]);
}

onMounted(async () => {
  const map = L.map(mapContainer.value).setView([52.473, 13.444], 13);
  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© OpenStreetMap contributors'
  }).addTo(map);

  const startLat = 52.47101094105251;
  const startLng = 13.442156578713048;
  const endLat = 52.475599175133986;
  const endLng = 13.450417095689525;

  const result = await createRoute(startLat, startLng, endLat, endLng);
  if (result) {
    createMap(map, result);
  }
});
</script>



<template>
  <div ref="mapContainer" style="height: 400px;"></div>
</template>

<style scoped>
@import url('https://unpkg.com/leaflet-routing-machine/dist/leaflet-routing-machine.css');
</style>

