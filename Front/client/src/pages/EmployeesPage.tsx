import { useState } from 'react';
import { employeeAPI } from '@/lib/api';
import { Employee } from '@/lib/types';
import { formatCurrency, formatCPF } from '@/lib/utils';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import LoadingSpinner from '@/components/LoadingSpinner';
import ErrorMessage from '@/components/ErrorMessage';
import EmptyState from '@/components/EmptyState';

export default function EmployeesPage() {
  const [employees, setEmployees] = useState<Employee[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Filter states
  const [supervisorCpf, setSupervisorCpf] = useState('');
  const [minProductivity, setMinProductivity] = useState('');
  const [maxProductivity, setMaxProductivity] = useState('');
  const [prodFromDate, setProdFromDate] = useState('');
  const [prodToDate, setProdToDate] = useState('');
  const [department, setDepartment] = useState('');

  const handleSearchBySupervisor = async () => {
    if (!supervisorCpf.trim()) {
      setError('Please enter a supervisor CPF');
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const response = await employeeAPI.getEmployeesBySupervisor(supervisorCpf);
      setEmployees(Array.isArray(response.data) ? response.data : [response.data]);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to fetch employees');
      setEmployees([]);
    } finally {
      setLoading(false);
    }
  };

  const handleSearchByProductivity = async () => {
    if (!minProductivity || !maxProductivity || !prodFromDate || !prodToDate) {
      setError('Please fill in all fields');
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const response = await employeeAPI.getEmployeesByProductivity(
        parseInt(minProductivity),
        parseInt(maxProductivity),
        prodFromDate,
        prodToDate
      );
      setEmployees(Array.isArray(response.data) ? response.data : [response.data]);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to fetch employees');
      setEmployees([]);
    } finally {
      setLoading(false);
    }
  };

  const handleSearchByDepartment = async () => {
    if (!department.trim()) {
      setError('Please enter a department localization');
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const response = await employeeAPI.getEmployeesByDepartment(department);
      setEmployees(Array.isArray(response.data) ? response.data : [response.data]);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to fetch employees');
      setEmployees([]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-6 max-w-7xl mx-auto">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-foreground mb-2">Employees</h1>
        <p className="text-muted-foreground">Manage and filter employees by various criteria</p>
      </div>

      {/* Filter Tabs */}
      <Tabs defaultValue="supervisor" className="mb-8">
        <TabsList className="grid w-full grid-cols-3">
          <TabsTrigger value="supervisor">By Supervisor</TabsTrigger>
          <TabsTrigger value="productivity">By Productivity</TabsTrigger>
          <TabsTrigger value="department">By Department</TabsTrigger>
        </TabsList>

        {/* By Supervisor */}
        <TabsContent value="supervisor">
          <Card>
            <CardHeader>
              <CardTitle>Search by Supervisor</CardTitle>
              <CardDescription>Enter a supervisor CPF to find their employees</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="flex gap-4">
                <Input
                  placeholder="Enter supervisor CPF..."
                  value={supervisorCpf}
                  onChange={(e) => setSupervisorCpf(e.target.value)}
                  onKeyPress={(e) => e.key === 'Enter' && handleSearchBySupervisor()}
                />
                <Button onClick={handleSearchBySupervisor} disabled={loading}>
                  {loading ? 'Searching...' : 'Search'}
                </Button>
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        {/* By Productivity */}
        <TabsContent value="productivity">
          <Card>
            <CardHeader>
              <CardTitle>Search by Productivity</CardTitle>
              <CardDescription>Filter employees by number of orders delivered in a date range</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="grid grid-cols-4 gap-4">
                <Input
                  placeholder="Min orders..."
                  type="number"
                  value={minProductivity}
                  onChange={(e) => setMinProductivity(e.target.value)}
                />
                <Input
                  placeholder="Max orders..."
                  type="number"
                  value={maxProductivity}
                  onChange={(e) => setMaxProductivity(e.target.value)}
                />
                <Input
                  placeholder="From date..."
                  type="datetime-local"
                  value={prodFromDate}
                  onChange={(e) => setProdFromDate(e.target.value)}
                />
                <Input
                  placeholder="To date..."
                  type="datetime-local"
                  value={prodToDate}
                  onChange={(e) => setProdToDate(e.target.value)}
                />
              </div>
              <Button onClick={handleSearchByProductivity} disabled={loading} className="mt-4">
                {loading ? 'Searching...' : 'Search'}
              </Button>
            </CardContent>
          </Card>
        </TabsContent>

        {/* By Department */}
        <TabsContent value="department">
          <Card>
            <CardHeader>
              <CardTitle>Search by Department Localization</CardTitle>
              <CardDescription>Enter a department localization to find its employees</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="flex gap-4">
                <Input
                  placeholder="Enter department localization..."
                  value={department}
                  onChange={(e) => setDepartment(e.target.value)}
                  onKeyPress={(e) => e.key === 'Enter' && handleSearchByDepartment()}
                />
                <Button onClick={handleSearchByDepartment} disabled={loading}>
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

      {!loading && employees.length === 0 && !error && (
        <EmptyState
          title="No employees found"
          description="Use the filters above to find employees"
        />
      )}

      {!loading && employees.length > 0 && (
        <Card>
          <CardHeader>
            <CardTitle>Results ({employees.length})</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="overflow-x-auto">
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Name</TableHead>
                    <TableHead>Role</TableHead>
                    <TableHead>CPF</TableHead>
                    <TableHead>Salary</TableHead>
                    <TableHead>Department</TableHead>
                    <TableHead>Manager CPF</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {employees.map((employee) => (
                    <TableRow key={employee.cpf}>
                      <TableCell className="font-medium">{employee.name}</TableCell>
                      <TableCell>{employee.role}</TableCell>
                      <TableCell className="font-mono text-xs">{employee.cpf}</TableCell>
                      <TableCell>${employee.salary.toFixed(2)}</TableCell>
                      <TableCell>{employee.departament_localization}</TableCell>
                      <TableCell className="font-mono text-xs">{employee.cpf_manager}</TableCell>
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
