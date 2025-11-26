import { useState } from 'react';
import { userAPI } from '@/lib/api';
import { User } from '@/lib/types';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import LoadingSpinner from '@/components/LoadingSpinner';
import ErrorMessage from '@/components/ErrorMessage';
import EmptyState from '@/components/EmptyState';

export default function UsersPage() {
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Filter states for Inactive Users
  const [inactiveFromDate, setInactiveFromDate] = useState('');
  const [inactiveToDate, setInactiveToDate] = useState('');

  // Filter states for Buyers by Price
  const [minPrice, setMinPrice] = useState('');
  const [maxPrice, setMaxPrice] = useState('');

  // Filter states for Buyers by Filter
  const [category, setCategory] = useState('');
  const [name, setName] = useState('');
  const [filterFromDate, setFilterFromDate] = useState('');
  const [filterToDate, setFilterToDate] = useState('');

  const handleSearchInactiveUsers = async () => {
    if (!inactiveFromDate || !inactiveToDate) {
      setError('Please fill in both date fields');
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const response = await userAPI.getInactiveUsers(inactiveFromDate, inactiveToDate);
      setUsers(Array.isArray(response.data) ? response.data : [response.data]);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to fetch inactive users');
      setUsers([]);
    } finally {
      setLoading(false);
    }
  };

  const handleSearchBuyersByPrice = async () => {
    if (!minPrice || !maxPrice) {
      setError('Please enter both minimum and maximum price');
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const response = await userAPI.getBuyersByPrice(parseFloat(minPrice), parseFloat(maxPrice));
      setUsers(Array.isArray(response.data) ? response.data : [response.data]);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to fetch buyers');
      setUsers([]);
    } finally {
      setLoading(false);
    }
  };

  const handleSearchBuyersByFilter = async () => {
    if (!filterFromDate || !filterToDate) {
      setError('Please fill in date range');
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const response = await userAPI.getBuyersByFilter(category, name, filterFromDate, filterToDate);
      setUsers(Array.isArray(response.data) ? response.data : [response.data]);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to fetch buyers');
      setUsers([]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-6 max-w-6xl mx-auto">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-foreground mb-2">Users</h1>
        <p className="text-muted-foreground">Search and filter users by various criteria</p>
      </div>

      {/* Error Message */}
      {error && <ErrorMessage message={error} />}

      {/* Filter Tabs */}
      <Tabs defaultValue="inactive" className="mb-8">
        <TabsList className="grid w-full grid-cols-3">
          <TabsTrigger value="inactive">Inactive Users</TabsTrigger>
          <TabsTrigger value="price">Buyers by Price</TabsTrigger>
          <TabsTrigger value="filter">Buyers by Filter</TabsTrigger>
        </TabsList>

        {/* Inactive Users */}
        <TabsContent value="inactive">
          <Card>
            <CardHeader>
              <CardTitle>Search Inactive Users</CardTitle>
              <CardDescription>Find users who were inactive during a specific date range</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="flex gap-4">
                <Input
                  placeholder="From date..."
                  type="datetime-local"
                  value={inactiveFromDate}
                  onChange={(e) => setInactiveFromDate(e.target.value)}
                />
                <Input
                  placeholder="To date..."
                  type="datetime-local"
                  value={inactiveToDate}
                  onChange={(e) => setInactiveToDate(e.target.value)}
                />
                <Button onClick={handleSearchInactiveUsers} disabled={loading}>
                  {loading ? 'Searching...' : 'Search'}
                </Button>
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        {/* Buyers by Price */}
        <TabsContent value="price">
          <Card>
            <CardHeader>
              <CardTitle>Search Buyers by Price Range</CardTitle>
              <CardDescription>Find users who made purchases within a price range</CardDescription>
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
                <Button onClick={handleSearchBuyersByPrice} disabled={loading}>
                  {loading ? 'Searching...' : 'Search'}
                </Button>
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        {/* Buyers by Filter */}
        <TabsContent value="filter">
          <Card>
            <CardHeader>
              <CardTitle>Search Buyers by Category and Name</CardTitle>
              <CardDescription>Find buyers by product category and/or name within a date range (leave empty to skip)</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="grid grid-cols-4 gap-4">
                <Input
                  placeholder="Category (optional)..."
                  value={category}
                  onChange={(e) => setCategory(e.target.value)}
                />
                <Input
                  placeholder="Name (optional)..."
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                />
                <Input
                  placeholder="From date..."
                  type="datetime-local"
                  value={filterFromDate}
                  onChange={(e) => setFilterFromDate(e.target.value)}
                />
                <Input
                  placeholder="To date..."
                  type="datetime-local"
                  value={filterToDate}
                  onChange={(e) => setFilterToDate(e.target.value)}
                />
              </div>
              <Button onClick={handleSearchBuyersByFilter} disabled={loading} className="mt-4">
                {loading ? 'Searching...' : 'Search'}
              </Button>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>

      {/* Results */}
      {loading && <LoadingSpinner />}

      {!loading && users.length === 0 && !error && (
        <EmptyState title="No users found" description="Try adjusting your search criteria" />
      )}

      {!loading && users.length > 0 && (
        <Card>
          <CardHeader>
            <CardTitle>Results ({users.length})</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="overflow-x-auto">
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>ID</TableHead>
                    <TableHead>First Name</TableHead>
                    <TableHead>Last Name</TableHead>
                    <TableHead>Address</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {users.map((user) => (
                    <TableRow key={user.id}>
                      <TableCell className="font-mono text-xs">{user.id}</TableCell>
                      <TableCell>{user.first_name?.trim() || '-'}</TableCell>
                      <TableCell>{user.last_name?.trim() || '-'}</TableCell>
                      <TableCell className="text-sm text-muted-foreground">{user.address?.trim() || '-'}</TableCell>
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
