<template>
  <div ref="mapContainer" style="height: 400px;"></div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import 'leaflet-routing-machine';
import axiosClient from '@/clients/axiosClient';

const props = defineProps({
  startLat: Number,
  startLng: Number,
  endLat: Number,
  endLng: Number
});

const mapContainer = ref(null);

async function createRoute(startLat, startLng, endLat, endLng) {
  try {
    const response = await axiosClient.post('/routes/api/v1/create', {
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
    console.error('Error fetching the route:', error);
    return null;
  }
}

function createMap(map, result) {
  let layer = new L.TileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png');
  map.addLayer(layer);

  let marker1 = L.marker([result.start.lat, result.start.lng]).addTo(map);
  let marker2 = L.marker([result.end.lat, result.end.lng]).addTo(map);

  let wp = result.waypoints.map(wp => L.latLng(wp.lat, wp.lng));

  L.Routing.control({
    waypoints: wp,
    createMarker: function (i, waypoint) {
      return L.marker(waypoint.latLng, {
        draggable: false,
        icon: L.divIcon({
          className: 'waypoint-icon',
          html: '<i class="fas fa-map-marker-alt"></i>'
        })
      });
    }
  }).addTo(map);

  map.fitBounds(L.latLngBounds([marker1.getLatLng(), marker2.getLatLng()]));
}

onMounted(async () => {
  const {startLat, startLng, endLat, endLng} = props;
  const centerLat = (startLat + endLat) / 2;
  const centerLng = (startLng + endLng) / 2;

  const map = L.map(mapContainer.value).setView([centerLat, centerLng], 13);
  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© OpenStreetMap contributors'
  }).addTo(map);

  const result = await createRoute(startLat, startLng, endLat, endLng);
  if (result) {
    createMap(map, result);
  }
});
</script>

<style scoped>
@import url('https://unpkg.com/leaflet-routing-machine/dist/leaflet-routing-machine.css');
</style>
