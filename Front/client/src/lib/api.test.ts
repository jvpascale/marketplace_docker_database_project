import { describe, it, expect, vi, beforeEach } from 'vitest';
import axios from 'axios';
import {
  userAPI,
  productAPI,
  orderAPI,
  employeeAPI,
  dependentAPI,
  departmentAPI,
} from './api';

// Mock axios
vi.mock('axios');
const mockedAxios = axios as any;

describe('API Service Layer', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  describe('User API', () => {
    it('should search users by last name', async () => {
      const mockData = [
        { id: '1', first_name: 'John', last_name: 'Doe', address: '123 Main St' },
      ];
      mockedAxios.create.mockReturnValue({
        post: vi.fn().mockResolvedValue({ data: mockData }),
      });

      const result = await userAPI.searchByLastName('Doe');
      expect(result.data).toEqual(mockData);
    });

    it('should get buyers by price', async () => {
      const mockData = { id: '1', first_name: 'Jane', last_name: 'Smith', address: '456 Oak Ave' };
      mockedAxios.create.mockReturnValue({
        post: vi.fn().mockResolvedValue({ data: mockData }),
      });

      const result = await userAPI.getBuyersByPrice(100);
      expect(result.data).toEqual(mockData);
    });

    it('should get buyers by filter', async () => {
      const mockData = [];
      mockedAxios.create.mockReturnValue({
        post: vi.fn().mockResolvedValue({ data: mockData }),
      });

      const result = await userAPI.getBuyersByFilter(
        'Electronics',
        'John',
        '2024-01-01T00:00:00Z',
        '2024-12-31T23:59:59Z'
      );
      expect(result.data).toEqual(mockData);
    });
  });

  describe('Product API', () => {
    it('should get products by seller', async () => {
      const mockData = [
        {
          id: '1',
          name: 'Product A',
          description: 'Test product',
          category: 'Electronics',
          price: 99.99,
          stock: 10,
          status: 'active',
          user_id: '1',
        },
      ];
      mockedAxios.create.mockReturnValue({
        post: vi.fn().mockResolvedValue({ data: mockData }),
      });

      const result = await productAPI.getProductsBySeller(1);
      expect(result.data).toEqual(mockData);
    });

    it('should get products by price range', async () => {
      const mockData = [];
      mockedAxios.create.mockReturnValue({
        post: vi.fn().mockResolvedValue({ data: mockData }),
      });

      const result = await productAPI.getProductsByPrice(10, 100);
      expect(result.data).toEqual(mockData);
    });

    it('should get products by sales quantity', async () => {
      const mockData = [];
      mockedAxios.create.mockReturnValue({
        post: vi.fn().mockResolvedValue({ data: mockData }),
      });

      const result = await productAPI.getProductsBySalesQuantity(
        5,
        20,
        '2024-01-01T00:00:00Z',
        '2024-12-31T23:59:59Z'
      );
      expect(result.data).toEqual(mockData);
    });
  });

  describe('Order API', () => {
    it('should get orders by user', async () => {
      const mockData = [
        {
          code: 1001,
          totalValue: 299.99,
          status: 'completed',
          creationDate: '2024-01-15T10:00:00Z',
          buyerId: 1,
          sellerId: 2,
          employeeCpf: '12345678901',
          paymentMethod: 'credit_card',
          installments: 3,
          destinationLocalization: 'New York',
          originLocalization: 'Los Angeles',
          originArrivalFlag: true,
          originDate: '2024-01-15T10:00:00Z',
          destinationArrivalFlag: false,
          destinationDate: '2024-01-17T15:00:00Z',
          vehiclePlate: 'ABC1234',
          deliveryDate: '2024-01-18T09:00:00Z',
          estimatedDeliveryDate: '2024-01-18T12:00:00Z',
        },
      ];
      mockedAxios.create.mockReturnValue({
        post: vi.fn().mockResolvedValue({ data: mockData }),
      });

      const result = await orderAPI.getOrdersByUser(1);
      expect(result.data).toEqual(mockData);
    });

    it('should get orders by status and date', async () => {
      const mockData = [];
      mockedAxios.create.mockReturnValue({
        post: vi.fn().mockResolvedValue({ data: mockData }),
      });

      const result = await orderAPI.getOrdersByStatus(
        'pending',
        '2024-01-01T00:00:00Z',
        '2024-12-31T23:59:59Z'
      );
      expect(result.data).toEqual(mockData);
    });

    it('should get orders by price range', async () => {
      const mockData = [];
      mockedAxios.create.mockReturnValue({
        post: vi.fn().mockResolvedValue({ data: mockData }),
      });

      const result = await orderAPI.getOrdersByPrice(100, 500);
      expect(result.data).toEqual(mockData);
    });

    it('should get orders by department', async () => {
      const mockData = [];
      mockedAxios.create.mockReturnValue({
        post: vi.fn().mockResolvedValue({ data: mockData }),
      });

      const result = await orderAPI.getOrdersByDepartment(
        'New York',
        '2024-01-01T00:00:00Z',
        '2024-12-31T23:59:59Z'
      );
      expect(result.data).toEqual(mockData);
    });

    it('should get orders by employee CPF', async () => {
      const mockData = [];
      mockedAxios.create.mockReturnValue({
        post: vi.fn().mockResolvedValue({ data: mockData }),
      });

      const result = await orderAPI.getOrdersByEmployee('12345678901');
      expect(result.data).toEqual(mockData);
    });
  });

  describe('Employee API', () => {
    it('should get employees by supervisor', async () => {
      const mockData = [
        {
          role: 'Manager',
          name: 'John Manager',
          salary: 5000,
          cpf: '12345678901',
          departament_localization: 'New York',
          cpf_manager: '98765432101',
        },
      ];
      mockedAxios.create.mockReturnValue({
        post: vi.fn().mockResolvedValue({ data: mockData }),
      });

      const result = await employeeAPI.getEmployeesBySupervisor('98765432101');
      expect(result.data).toEqual(mockData);
    });

    it('should get employees by productivity', async () => {
      const mockData = [];
      mockedAxios.create.mockReturnValue({
        post: vi.fn().mockResolvedValue({ data: mockData }),
      });

      const result = await employeeAPI.getEmployeesByProductivity(
        10,
        50,
        '2024-01-01T00:00:00Z',
        '2024-12-31T23:59:59Z'
      );
      expect(result.data).toEqual(mockData);
    });

    it('should get employees by department', async () => {
      const mockData = [];
      mockedAxios.create.mockReturnValue({
        post: vi.fn().mockResolvedValue({ data: mockData }),
      });

      const result = await employeeAPI.getEmployeesByDepartment('New York');
      expect(result.data).toEqual(mockData);
    });
  });

  describe('Dependent API', () => {
    it('should get dependents by unit', async () => {
      const mockData = [
        {
          name: 'Child Name',
          age: 10,
          kinship: 'Son',
          cpfEmployee: '12345678901',
        },
      ];
      mockedAxios.create.mockReturnValue({
        post: vi.fn().mockResolvedValue({ data: mockData }),
      });

      const result = await dependentAPI.getDependentsByUnit('New York');
      expect(result.data).toEqual(mockData);
    });

    it('should get minor children', async () => {
      const mockData = [];
      mockedAxios.create.mockReturnValue({
        post: vi.fn().mockResolvedValue({ data: mockData }),
      });

      const result = await dependentAPI.getMinorChildren();
      expect(result.data).toEqual(mockData);
    });

    it('should get dependents by employee', async () => {
      const mockData = [];
      mockedAxios.create.mockReturnValue({
        post: vi.fn().mockResolvedValue({ data: mockData }),
      });

      const result = await dependentAPI.getDependentsByEmployee('12345678901');
      expect(result.data).toEqual(mockData);
    });
  });

  describe('Department API', () => {
    it('should get origin destination by order', async () => {
      const mockData = [
        {
          number: 1,
          localization: 'New York',
          name: 'NY Department',
          cpf_manager: '12345678901',
        },
      ];
      mockedAxios.create.mockReturnValue({
        post: vi.fn().mockResolvedValue({ data: mockData }),
      });

      const result = await departmentAPI.getOriginDestinationByOrder(1001);
      expect(result.data).toEqual(mockData);
    });

    it('should get departments by order quantity', async () => {
      const mockData = [];
      mockedAxios.create.mockReturnValue({
        post: vi.fn().mockResolvedValue({ data: mockData }),
      });

      const result = await departmentAPI.getDepartmentsByOrderQuantity(
        10,
        100,
        '2024-01-01T00:00:00Z',
        '2024-12-31T23:59:59Z'
      );
      expect(result.data).toEqual(mockData);
    });

    it('should get departments by employee quantity', async () => {
      const mockData = [];
      mockedAxios.create.mockReturnValue({
        post: vi.fn().mockResolvedValue({ data: mockData }),
      });

      const result = await departmentAPI.getDepartmentsByEmployeeQuantity(5, 50);
      expect(result.data).toEqual(mockData);
    });
  });
});
