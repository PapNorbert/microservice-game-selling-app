import axios from 'axios'
import { serverUrl } from '../config/application.json'

const configuredAxios = axios.create({
  baseURL: serverUrl,
  headers: { 'Content-Type': 'application/json' },
  withCredentials: true
});

export default configuredAxios;