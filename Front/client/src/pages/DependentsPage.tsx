import { useState } from 'react';
import { dependentAPI } from '@/lib/api';
import { Dependent } from '@/lib/types';
import { formatCPF } from '@/lib/utils';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import LoadingSpinner from '@/components/LoadingSpinner';
import ErrorMessage from '@/components/ErrorMessage';
import EmptyState from '@/components/EmptyState';

export default function DependentsPage() {
  const [dependents, setDependents] = useState<Dependent[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Filter states
  const [department, setDepartment] = useState('');
  const [employeeCpf, setEmployeeCpf] = useState('');

  const handleSearchByDepartment = async () => {
    if (!department.trim()) {
      setError('Please enter a department');
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const response = await dependentAPI.getDependentsByUnit(department);
      setDependents(Array.isArray(response.data) ? response.data : [response.data]);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to fetch dependents');
      setDependents([]);
    } finally {
      setLoading(false);
    }
  };

  const handleGetMinors = async () => {
    setLoading(true);
    setError(null);

    try {
      const response = await dependentAPI.getMinorChildren();
      setDependents(Array.isArray(response.data) ? response.data : [response.data]);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to fetch minors');
      setDependents([]);
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
      const response = await dependentAPI.getDependentsByEmployee(employeeCpf);
      setDependents(Array.isArray(response.data) ? response.data : [response.data]);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to fetch dependents');
      setDependents([]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-6 max-w-6xl mx-auto">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-foreground mb-2">Dependents</h1>
        <p className="text-muted-foreground">View dependents and family information</p>
      </div>

      {/* Filter Tabs */}
      <Tabs defaultValue="department" className="mb-8">
        <TabsList className="grid w-full grid-cols-3">
          <TabsTrigger value="department">By Department</TabsTrigger>
          <TabsTrigger value="minors">Minor Children</TabsTrigger>
          <TabsTrigger value="employee">By Employee</TabsTrigger>
        </TabsList>

        {/* By Department */}
        <TabsContent value="department">
          <Card>
            <CardHeader>
              <CardTitle>Search by Department</CardTitle>
              <CardDescription>Enter a department to find dependents</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="flex gap-4">
                <Input
                  placeholder="Enter department..."
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

        {/* Minor Children */}
        <TabsContent value="minors">
          <Card>
            <CardHeader>
              <CardTitle>Minor Children</CardTitle>
              <CardDescription>View all minor children in the system</CardDescription>
            </CardHeader>
            <CardContent>
              <Button onClick={handleGetMinors} disabled={loading}>
                {loading ? 'Loading...' : 'Get Minor Children'}
              </Button>
            </CardContent>
          </Card>
        </TabsContent>

        {/* By Employee */}
        <TabsContent value="employee">
          <Card>
            <CardHeader>
              <CardTitle>Search by Employee</CardTitle>
              <CardDescription>Enter an employee CPF to find their dependents</CardDescription>
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

      {!loading && dependents.length === 0 && !error && (
        <EmptyState
          title="No dependents found"
          description="Use the filters above to find dependents"
        />
      )}

      {!loading && dependents.length > 0 && (
        <Card>
          <CardHeader>
            <CardTitle>Results ({dependents.length})</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="overflow-x-auto">
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Name</TableHead>
                    <TableHead>Age</TableHead>
                    <TableHead>Kinship</TableHead>
                    <TableHead>Employee CPF</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {dependents.map((dependent, index) => (
                    <TableRow key={`${dependent.cpfEmployee}-${index}`}>
                      <TableCell className="font-medium">{dependent.name}</TableCell>
                      <TableCell>{dependent.age} years</TableCell>
                      <TableCell>{dependent.kinship}</TableCell>
                      <TableCell className="font-mono text-xs">{dependent.cpfEmployee}</TableCell>
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
