import axios, { AxiosInstance } from 'axios';
import { prepareRequestPayload } from './utils';

// API base URL configured to connect to localhost:8080
const API_BASE_URL = 'http://localhost:8080';

const apiClient: AxiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// User API endpoints
export const userAPI = {
  searchByLastName: (lastname: string) =>
    apiClient.post('/users/search', prepareRequestPayload({ lastname })),
  
  getBuyersByPrice: (id: number) =>
    apiClient.post('/users/buyers/price', prepareRequestPayload({ id })),
  
  getBuyersByFilter: (category: string, name: string, from: string, to: string) =>
    apiClient.post('/users/buyers/filter', prepareRequestPayload({ category, name, from, to })),
};

// Product API endpoints
export const productAPI = {
  getProductsBySeller: (sellerId: number) =>
    apiClient.post('/products/seller', prepareRequestPayload({ sellerId })),
  
  getProductsBySalesQuantity: (min: number, max: number, from: string, to: string) =>
    apiClient.post('/products/filter/sales', prepareRequestPayload({ min, max, from, to })),
  
  getProductsByPrice: (minPrice: number, maxPrice: number) =>
    apiClient.post('/products/filter/price', prepareRequestPayload({ minPrice, maxPrice })),
};

// Order API endpoints
export const orderAPI = {
  getOrdersByUser: (id: number) =>
    apiClient.post('/orders/user', prepareRequestPayload({ id })),
  
  getOrdersByStatus: (status: string, from: string, to: string) =>
    apiClient.post('/orders/filter/status', prepareRequestPayload({ status, from, to })),
  
  getOrdersByPrice: (minPrice: number, maxPrice: number) =>
    apiClient.post('/orders/filter/price', prepareRequestPayload({ minPrice, maxPrice })),
  
  getOrdersByDepartment: (localization: string, from: string, to: string) =>
    apiClient.post('/orders/filter/department', prepareRequestPayload({ localization, from, to })),
  
  getOrdersByEmployee: (cpf: string) =>
    apiClient.post('/orders/employee', prepareRequestPayload({ cpf })),
};

// Employee API endpoints
export const employeeAPI = {
  getEmployeesBySupervisor: (supervisorCpf: string) =>
    apiClient.post('/employees/filter/supervisor', prepareRequestPayload({ supervisorCpf })),
  
  getEmployeesByProductivity: (min: number, max: number, from: string, to: string) =>
    apiClient.post('/employees/filter/productivity', prepareRequestPayload({ min, max, from, to })),
  
  getEmployeesByDepartment: (localization: string) =>
    apiClient.post('/employees/filter/department', prepareRequestPayload({ localization })),
};

// Dependent API endpoints
export const dependentAPI = {
  getDependentsByUnit: (unitLocalization: string) =>
    apiClient.post('/dependents/unit', prepareRequestPayload({ unitLocalization })),
  
  getMinorChildren: () =>
    apiClient.post('/dependents/minors', prepareRequestPayload({})),
  
  getDependentsByEmployee: (employeeCpf: string) =>
    apiClient.post('/dependents/employee', prepareRequestPayload({ employeeCpf })),
};

// Department API endpoints
export const departmentAPI = {
  getOriginDestinationByOrder: (orderCode: number) =>
    apiClient.post('/departments/order-route', prepareRequestPayload({ orderCode })),
  
  getDepartmentsByOrderQuantity: (minOrders: number, maxOrders: number, from: string, to: string) =>
    apiClient.post('/departments/filter/order-quantity', prepareRequestPayload({ minOrders, maxOrders, from, to })),
  
  getDepartmentsByEmployeeQuantity: (min: number, max: number) =>
    apiClient.post('/departments/filter/employee-quantity', prepareRequestPayload({ min, max })),
};

export default apiClient;
