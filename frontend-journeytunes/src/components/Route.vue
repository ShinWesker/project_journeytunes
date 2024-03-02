<template>
  <div ref="mapContainer" style="height: 400px;"></div>
</template>

<script setup>
import { onMounted, ref, watch, reactive } from 'vue';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import 'leaflet-routing-machine';
import axiosClient from "@/clients/axiosClient";

const props = defineProps({
  startLat: Number,
  startLng: Number,
  endLat: Number,
  endLng: Number
});

const mapContainer = ref(null);
let map = null;
let markers = [];
let routingControl = null;

async function fetchRoute(startLat, startLng, endLat, endLng) {
  const response = await axiosClient.post('/routes/api/v1/create', {
    start: {
      lat: startLat.toString(),
      lng: startLng.toString()
    },
    end: {
      lat: endLat.toString(),
      lng: endLng.toString()
    }
  });
  return response.data;
}

function clearMapContents() {
  // Remove existing markers
  markers.forEach(marker => map.removeLayer(marker));
  markers = [];

  // Remove existing routing control
  if (routingControl) {
    map.removeControl(routingControl);
    routingControl = null;
  }
}

function createMap(result) {
  if (!map) {
    map = L.map(mapContainer.value).setView([result.start.lat, result.start.lng], 10);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(map);
  }

  clearMapContents();

  // Add new markers
  let marker1 = L.marker([result.start.lat, result.start.lng]).addTo(map);
  let marker2 = L.marker([result.end.lat, result.end.lng]).addTo(map);
  markers.push(marker1, marker2);

  // Create routing control
  routingControl = L.Routing.control({
    waypoints: [
      marker1.getLatLng(),
      marker2.getLatLng()
    ],
    show: false // This hides the routing instructions
  }).addTo(map);

  map.fitBounds(L.latLngBounds([
    [result.start.lat, result.start.lng],
    [result.end.lat, result.end.lng]
  ]), {
    padding: [25, 25]
  });
}

onMounted(async () => {
  const { startLat, startLng, endLat, endLng } = props;
  try {
    const routeResult = await fetchRoute(startLat, startLng, endLat, endLng);
    if (routeResult) {
      createMap(routeResult);
    }
  } catch (error) {
    console.error('Failed to fetch route:', error);
  }
});

watch(() => [props.startLat, props.startLng, props.endLat, props.endLng], async () => {
  try {
    const routeResult = await fetchRoute(props.startLat, props.startLng, props.endLat, props.endLng);
    if (routeResult) {
      createMap(routeResult);
    }
  } catch (error) {
    console.error('Failed to fetch route:', error);
  }
}, { deep: true });
</script>



<style scoped>
@import url('https://unpkg.com/leaflet-routing-machine/dist/leaflet-routing-machine.css');
</style>
