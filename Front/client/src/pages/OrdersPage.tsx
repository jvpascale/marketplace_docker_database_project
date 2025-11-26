import { useState } from 'react';
import { orderAPI } from '@/lib/api';
import { Order } from '@/lib/types';
import { formatCurrency, formatDateForDisplay } from '@/lib/utils';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import LoadingSpinner from '@/components/LoadingSpinner';
import ErrorMessage from '@/components/ErrorMessage';
import EmptyState from '@/components/EmptyState';

export default function OrdersPage() {
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Filter states
  const [userId, setUserId] = useState('');
  const [status, setStatus] = useState('');
  const [statusFromDate, setStatusFromDate] = useState('');
  const [statusToDate, setStatusToDate] = useState('');
  const [minPrice, setMinPrice] = useState('');
  const [maxPrice, setMaxPrice] = useState('');
  const [department, setDepartment] = useState('');
  const [deptFromDate, setDeptFromDate] = useState('');
  const [deptToDate, setDeptToDate] = useState('');
  const [employeeCpf, setEmployeeCpf] = useState('');

  const handleSearchByUser = async () => {
    if (!userId.trim()) {
      setError('Please enter a user ID');
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const response = await orderAPI.getOrdersByUser(parseInt(userId));
      setOrders(Array.isArray(response.data) ? response.data : [response.data]);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to fetch orders');
      setOrders([]);
    } finally {
      setLoading(false);
    }
  };

  const handleSearchByStatus = async () => {
    if (!status || !statusFromDate || !statusToDate) {
      setError('Please fill in all fields');
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const response = await orderAPI.getOrdersByStatus(status, statusFromDate, statusToDate);
      setOrders(Array.isArray(response.data) ? response.data : [response.data]);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to fetch orders');
      setOrders([]);
    } finally {
      setLoading(false);
    }
  };

  const handleSearchByPrice = async () => {
    if (!minPrice || !maxPrice) {
      setError('Please enter both minimum and maximum price');
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const response = await orderAPI.getOrdersByPrice(parseFloat(minPrice), parseFloat(maxPrice));
      setOrders(Array.isArray(response.data) ? response.data : [response.data]);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to fetch orders');
      setOrders([]);
    } finally {
      setLoading(false);
    }
  };

  const handleSearchByDepartment = async () => {
    if (!department || !deptFromDate || !deptToDate) {
      setError('Please fill in department localization and date range');
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const response = await orderAPI.getOrdersByDepartment(department, deptFromDate, deptToDate);
      setOrders(Array.isArray(response.data) ? response.data : [response.data]);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to fetch orders');
      setOrders([]);
    } finally {
      setLoading(false);
    }
  };

  const handleSearchByEmployee = async () => {
    if (!employeeCpf.trim()) {
      setError('Please enter an employee CPF');
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const response = await orderAPI.getOrdersByEmployee(employeeCpf);
      setOrders(Array.isArray(response.data) ? response.data : [response.data]);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to fetch orders');
      setOrders([]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-6 max-w-7xl mx-auto">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-foreground mb-2">Orders</h1>
        <p className="text-muted-foreground">View and filter orders by multiple criteria</p>
      </div>

      {/* Filter Tabs */}
      <Tabs defaultValue="user" className="mb-8">
        <TabsList className="grid w-full grid-cols-5">
          <TabsTrigger value="user">By User</TabsTrigger>
          <TabsTrigger value="status">By Status</TabsTrigger>
          <TabsTrigger value="price">By Price</TabsTrigger>
          <TabsTrigger value="department">By Department</TabsTrigger>
          <TabsTrigger value="employee">By Employee</TabsTrigger>
        </TabsList>

        {/* By User */}
        <TabsContent value="user">
          <Card>
            <CardHeader>
              <CardTitle>Search by User</CardTitle>
              <CardDescription>Enter a user ID to find their orders</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="flex gap-4">
                <Input
                  placeholder="Enter user ID..."
                  type="number"
                  value={userId}
                  onChange={(e) => setUserId(e.target.value)}
                  onKeyPress={(e) => e.key === 'Enter' && handleSearchByUser()}
                />
                <Button onClick={handleSearchByUser} disabled={loading}>
                  {loading ? 'Searching...' : 'Search'}
                </Button>
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        {/* By Status */}
        <TabsContent value="status">
          <Card>
            <CardHeader>
              <CardTitle>Search by Status</CardTitle>
              <CardDescription>Filter orders by status and date range</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="grid grid-cols-3 gap-4">
                <Input
                  placeholder="Status (e.g., pending)..."
                  value={status}
                  onChange={(e) => setStatus(e.target.value)}
                />
                <Input
                  placeholder="From date..."
                  type="datetime-local"
                  value={statusFromDate}
                  onChange={(e) => setStatusFromDate(e.target.value)}
                />
                <Input
                  placeholder="To date..."
                  type="datetime-local"
                  value={statusToDate}
                  onChange={(e) => setStatusToDate(e.target.value)}
                />
              </div>
              <Button onClick={handleSearchByStatus} disabled={loading} className="mt-4">
                {loading ? 'Searching...' : 'Search'}
              </Button>
            </CardContent>
          </Card>
        </TabsContent>

        {/* By Price */}
        <TabsContent value="price">
          <Card>
            <CardHeader>
              <CardTitle>Search by Price Range</CardTitle>
              <CardDescription>Enter minimum and maximum order value</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="flex gap-4">
                <Input
                  placeholder="Min price..."
                  type="number"
                  step="0.01"
                  value={minPrice}
                  onChange={(e) => setMinPrice(e.target.value)}
                />
                <Input
                  placeholder="Max price..."
                  type="number"
                  step="0.01"
                  value={maxPrice}
                  onChange={(e) => setMaxPrice(e.target.value)}
                />
                <Button onClick={handleSearchByPrice} disabled={loading}>
                  {loading ? 'Searching...' : 'Search'}
                </Button>
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        {/* By Department */}
        <TabsContent value="department">
          <Card>
            <CardHeader>
              <CardTitle>Search by Department Localization</CardTitle>
              <CardDescription>Filter orders by department localization and date range</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="grid grid-cols-3 gap-4">
                <Input
                  placeholder="Department localization..."
                  value={department}
                  onChange={(e) => setDepartment(e.target.value)}
                />
                <Input
                  placeholder="From date..."
                  type="datetime-local"
                  value={deptFromDate}
                  onChange={(e) => setDeptFromDate(e.target.value)}
                />
                <Input
                  placeholder="To date..."
                  type="datetime-local"
                  value={deptToDate}
                  onChange={(e) => setDeptToDate(e.target.value)}
                />
              </div>
              <Button onClick={handleSearchByDepartment} disabled={loading} className="mt-4">
                {loading ? 'Searching...' : 'Search'}
              </Button>
            </CardContent>
          </Card>
        </TabsContent>

        {/* By Employee */}
        <TabsContent value="employee">
          <Card>
            <CardHeader>
              <CardTitle>Search by Employee</CardTitle>
              <CardDescription>Enter employee CPF to find their orders</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="flex gap-4">
                <Input
                  placeholder="Enter employee CPF..."
                  value={employeeCpf}
                  onChange={(e) => setEmployeeCpf(e.target.value)}
                  onKeyPress={(e) => e.key === 'Enter' && handleSearchByEmployee()}
                />
                <Button onClick={handleSearchByEmployee} disabled={loading}>
                  {loading ? 'Searching...' : 'Search'}
                </Button>
              </div>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>

      {/* Error Message */}
      {error && <ErrorMessage message={error} />}

      {/* Results */}
      {loading && <LoadingSpinner />}

      {!loading && orders.length === 0 && !error && (
        <EmptyState
          title="No orders found"
          description="Use the filters above to find orders"
        />
      )}

      {!loading && orders.length > 0 && (
        <Card>
          <CardHeader>
            <CardTitle>Results ({orders.length})</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="overflow-x-auto">
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Order Code</TableHead>
                    <TableHead>Total Value</TableHead>
                    <TableHead>Status</TableHead>
                    <TableHead>Creation Date</TableHead>
                    <TableHead>Buyer ID</TableHead>
                    <TableHead>Seller ID</TableHead>
                    <TableHead>Employee CPF</TableHead>
                    <TableHead>Payment Method</TableHead>
                    <TableHead>Installments</TableHead>
                    <TableHead>Origin Location</TableHead>
                    <TableHead>Destination Location</TableHead>
                    <TableHead>Origin Date</TableHead>
                    <TableHead>Destination Date</TableHead>
                    <TableHead>Delivery Date</TableHead>
                    <TableHead>Estimated Delivery</TableHead>
                    <TableHead>Vehicle Plate</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {orders.map((order) => (
                    <TableRow key={order.code}>
                      <TableCell className="font-mono">{order.code}</TableCell>
                      <TableCell>{formatCurrency(order.totalValue)}</TableCell>
                      <TableCell>
                        <span className={`px-2 py-1 rounded text-xs font-medium ${
                          order.status === 'completed'
                            ? 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200'
                            : order.status === 'pending'
                            ? 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200'
                            : 'bg-gray-100 text-gray-800 dark:bg-gray-900 dark:text-gray-200'
                        }`}>
                          {order.status?.trim() || '-'}
                        </span>
                      </TableCell>
                      <TableCell className="text-sm">
                        {formatDateForDisplay(order.creationDate)}
                      </TableCell>
                      <TableCell>{order.buyerId}</TableCell>
                      <TableCell>{order.sellerId}</TableCell>
                      <TableCell className="font-mono text-xs">{order.employeeCpf?.trim() || '-'}</TableCell>
                      <TableCell>{order.paymentMethod?.trim() || '-'}</TableCell>
                      <TableCell>{order.installments}</TableCell>
                      <TableCell>{order.originLocalization?.trim() || '-'}</TableCell>
                      <TableCell>{order.destinationLocalization?.trim() || '-'}</TableCell>
                      <TableCell className="text-sm">{formatDateForDisplay(order.originDate)}</TableCell>
                      <TableCell className="text-sm">{formatDateForDisplay(order.destinationDate)}</TableCell>
                      <TableCell className="text-sm">{formatDateForDisplay(order.deliveryDate)}</TableCell>
                      <TableCell className="text-sm">{formatDateForDisplay(order.estimatedDeliveryDate)}</TableCell>
                      <TableCell className="font-mono text-xs">{order.vehiclePlate?.trim() || '-'}</TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </div>
          </CardContent>
        </Card>
      )}
    </div>
  );
}
