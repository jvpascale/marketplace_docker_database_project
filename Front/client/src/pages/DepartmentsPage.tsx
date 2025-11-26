import { useState } from 'react';
import { departmentAPI } from '@/lib/api';
import { Department } from '@/lib/types';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import LoadingSpinner from '@/components/LoadingSpinner';
import ErrorMessage from '@/components/ErrorMessage';
import EmptyState from '@/components/EmptyState';

export default function DepartmentsPage() {
  const [departments, setDepartments] = useState<Department[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Filter states
  const [orderCode, setOrderCode] = useState('');
  const [minOrders, setMinOrders] = useState('');
  const [maxOrders, setMaxOrders] = useState('');
  const [orderFromDate, setOrderFromDate] = useState('');
  const [orderToDate, setOrderToDate] = useState('');
  const [minEmployees, setMinEmployees] = useState('');
  const [maxEmployees, setMaxEmployees] = useState('');

  const handleSearchByOrderCode = async () => {
    if (!orderCode.trim()) {
      setError('Please enter a department localization');
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const response = await departmentAPI.getOriginDestinationByOrder(parseInt(orderCode));
      setDepartments(Array.isArray(response.data) ? response.data : [response.data]);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to fetch departments');
      setDepartments([]);
    } finally {
      setLoading(false);
    }
  };

  const handleSearchByOrderQuantity = async () => {
    if (!orderCode || !minOrders || !maxOrders || !orderFromDate || !orderToDate) {
      setError('Please fill in all fields including department localization');
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const response = await departmentAPI.getDepartmentsByOrderQuantity(
        parseInt(minOrders),
        parseInt(maxOrders),
        orderFromDate,
        orderToDate
      );
      setDepartments(Array.isArray(response.data) ? response.data : [response.data]);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to fetch departments');
      setDepartments([]);
    } finally {
      setLoading(false);
    }
  };

  const handleSearchByEmployeeQuantity = async () => {
    if (!minEmployees || !maxEmployees) {
      setError('Please enter both minimum and maximum employee count');
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const response = await departmentAPI.getDepartmentsByEmployeeQuantity(
        parseInt(minEmployees),
        parseInt(maxEmployees)
      );
      setDepartments(Array.isArray(response.data) ? response.data : [response.data]);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to fetch departments');
      setDepartments([]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-6 max-w-6xl mx-auto">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-foreground mb-2">Departments</h1>
        <p className="text-muted-foreground">Explore departments and their operations</p>
      </div>

      {/* Filter Tabs */}
      <Tabs defaultValue="order-code" className="mb-8">
        <TabsList className="grid w-full grid-cols-3">
          <TabsTrigger value="order-code">By Order Code</TabsTrigger>
          <TabsTrigger value="order-quantity">By Order Quantity</TabsTrigger>
          <TabsTrigger value="employee-quantity">By Employee Quantity</TabsTrigger>
        </TabsList>

        {/* By Order Code */}
        <TabsContent value="order-code">
          <Card>
            <CardHeader>
              <CardTitle>Search by Department Localization</CardTitle>
              <CardDescription>Enter a department localization to find its departments</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="flex gap-4">
                <Input
                  placeholder="Enter department localization..."
                  type="text"
                  value={orderCode}
                  onChange={(e) => setOrderCode(e.target.value)}
                  onKeyPress={(e) => e.key === 'Enter' && handleSearchByOrderCode()}
                />
                <Button onClick={handleSearchByOrderCode} disabled={loading}>
                  {loading ? 'Searching...' : 'Search'}
                </Button>
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        {/* By Order Quantity */}
        <TabsContent value="order-quantity">
          <Card>
            <CardHeader>
              <CardTitle>Search by Order Quantity</CardTitle>
              <CardDescription>Filter departments by order quantity, localization, and date range</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="grid grid-cols-5 gap-4">
                <Input
                  placeholder="Department localization..."
                  type="text"
                  value={orderCode}
                  onChange={(e) => setOrderCode(e.target.value)}
                />
                <Input
                  placeholder="Min orders..."
                  type="number"
                  value={minOrders}
                  onChange={(e) => setMinOrders(e.target.value)}
                />
                <Input
                  placeholder="Max orders..."
                  type="number"
                  value={maxOrders}
                  onChange={(e) => setMaxOrders(e.target.value)}
                />
                <Input
                  placeholder="From date..."
                  type="datetime-local"
                  value={orderFromDate}
                  onChange={(e) => setOrderFromDate(e.target.value)}
                />
                <Input
                  placeholder="To date..."
                  type="datetime-local"
                  value={orderToDate}
                  onChange={(e) => setOrderToDate(e.target.value)}
                />
              </div>
              <Button onClick={handleSearchByOrderQuantity} disabled={loading} className="mt-4">
                {loading ? 'Searching...' : 'Search'}
              </Button>
            </CardContent>
          </Card>
        </TabsContent>

        {/* By Employee Quantity */}
        <TabsContent value="employee-quantity">
          <Card>
            <CardHeader>
              <CardTitle>Search by Employee Quantity</CardTitle>
              <CardDescription>Filter departments by number of employees</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="flex gap-4">
                <Input
                  placeholder="Min employees..."
                  type="number"
                  value={minEmployees}
                  onChange={(e) => setMinEmployees(e.target.value)}
                />
                <Input
                  placeholder="Max employees..."
                  type="number"
                  value={maxEmployees}
                  onChange={(e) => setMaxEmployees(e.target.value)}
                />
                <Button onClick={handleSearchByEmployeeQuantity} disabled={loading}>
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

      {!loading && departments.length === 0 && !error && (
        <EmptyState
          title="No departments found"
          description="Use the filters above to find departments"
        />
      )}

      {!loading && departments.length > 0 && (
        <Card>
          <CardHeader>
            <CardTitle>Results ({departments.length})</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="overflow-x-auto">
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Number</TableHead>
                    <TableHead>Name</TableHead>
                    <TableHead>Localization</TableHead>
                    <TableHead>Manager CPF</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {departments.map((dept) => (
                    <TableRow key={dept.number}>
                      <TableCell className="font-mono">{dept.number}</TableCell>
                      <TableCell className="font-medium">{dept.name}</TableCell>
                      <TableCell>{dept.localization}</TableCell>
                      <TableCell className="font-mono text-xs">{dept.cpf_manager}</TableCell>
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
