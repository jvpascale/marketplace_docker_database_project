// User types
export interface User {
  id: string;
  address: string;
  first_name: string;
  last_name: string;
}

// Product types
export interface Product {
  id: string;
  name: string;
  description: string;
  category: string;
  price: number;
  stock: number;
  status: string;
  user_id: string;
}

// Order types
export interface Order {
  code: number;
  totalValue: number;
  status: string;
  creationDate: string;
  buyerId: number;
  sellerId: number;
  employeeCpf: string;
  paymentMethod: string;
  installments: number;
  destinationLocalization: string;
  originLocalization: string;
  originArrivalFlag: boolean;
  originDate: string;
  destinationArrivalFlag: boolean;
  destinationDate: string;
  vehiclePlate: string;
  deliveryDate: string;
  estimatedDeliveryDate: string;
}

// Employee types
export interface Employee {
  role: string;
  name: string;
  salary: number;
  cpf: string;
  departament_localization: string;
  cpf_manager: string;
}

// Dependent types
export interface Dependent {
  name: string;
  age: number;
  kinship: string;
  cpfEmployee: string;
}

// Department types
export interface Department {
  number: number;
  localization: string;
  name: string;
  cpf_manager: string;
}

// API Response types
export type ApiResponse<T> = T | T[];
