import { useState } from 'react';
import { productAPI } from '@/lib/api';
import { Product } from '@/lib/types';
import { formatCurrency } from '@/lib/utils';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import LoadingSpinner from '@/components/LoadingSpinner';
import ErrorMessage from '@/components/ErrorMessage';
import EmptyState from '@/components/EmptyState';

export default function ProductsPage() {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Filter states
  const [sellerId, setSellerId] = useState('');
  const [minPrice, setMinPrice] = useState('');
  const [maxPrice, setMaxPrice] = useState('');
  const [minSales, setMinSales] = useState('');
  const [maxSales, setMaxSales] = useState('');
  const [fromDate, setFromDate] = useState('');
  const [toDate, setToDate] = useState('');

  const handleSearchBySeller = async () => {
    if (!sellerId.trim()) {
      setError('Please enter a seller ID');
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const response = await productAPI.getProductsBySeller(parseInt(sellerId));
      setProducts(Array.isArray(response.data) ? response.data : [response.data]);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to fetch products');
      setProducts([]);
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
      const response = await productAPI.getProductsByPrice(
        parseFloat(minPrice),
        parseFloat(maxPrice)
      );
      setProducts(Array.isArray(response.data) ? response.data : [response.data]);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to fetch products');
      setProducts([]);
    } finally {
      setLoading(false);
    }
  };

  const handleSearchBySalesQuantity = async () => {
    if (!minSales || !maxSales || !fromDate || !toDate) {
      setError('Please fill in all fields');
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const response = await productAPI.getProductsBySalesQuantity(
        parseInt(minSales),
        parseInt(maxSales),
        fromDate,
        toDate
      );
      setProducts(Array.isArray(response.data) ? response.data : [response.data]);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to fetch products');
      setProducts([]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-6 max-w-6xl mx-auto">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-foreground mb-2">Products</h1>
        <p className="text-muted-foreground">Browse and filter products by various criteria</p>
      </div>

      {/* Filter Tabs */}
      <Tabs defaultValue="seller" className="mb-8">
        <TabsList className="grid w-full grid-cols-3">
          <TabsTrigger value="seller">By Seller</TabsTrigger>
          <TabsTrigger value="price">By Price</TabsTrigger>
          <TabsTrigger value="sales">By Sales Quantity</TabsTrigger>
        </TabsList>

        {/* By Seller */}
        <TabsContent value="seller">
          <Card>
            <CardHeader>
              <CardTitle>Search by Seller</CardTitle>
              <CardDescription>Enter a seller ID to find their products</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="flex gap-4">
                <Input
                  placeholder="Enter seller ID..."
                  type="number"
                  value={sellerId}
                  onChange={(e) => setSellerId(e.target.value)}
                  onKeyPress={(e) => e.key === 'Enter' && handleSearchBySeller()}
                />
                <Button onClick={handleSearchBySeller} disabled={loading}>
                  {loading ? 'Searching...' : 'Search'}
                </Button>
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        {/* By Price */}
        <TabsContent value="price">
          <Card>
            <CardHeader>
              <CardTitle>Search by Price Range</CardTitle>
              <CardDescription>Enter minimum and maximum price</CardDescription>
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

        {/* By Sales Quantity */}
        <TabsContent value="sales">
          <Card>
            <CardHeader>
              <CardTitle>Search by Sales Quantity</CardTitle>
              <CardDescription>Filter by sales quantity within a date range</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                <Input
                  placeholder="Min sales..."
                  type="number"
                  value={minSales}
                  onChange={(e) => setMinSales(e.target.value)}
                />
                <Input
                  placeholder="Max sales..."
                  type="number"
                  value={maxSales}
                  onChange={(e) => setMaxSales(e.target.value)}
                />
                <Input
                  placeholder="From date..."
                  type="datetime-local"
                  value={fromDate}
                  onChange={(e) => setFromDate(e.target.value)}
                />
                <Input
                  placeholder="To date..."
                  type="datetime-local"
                  value={toDate}
                  onChange={(e) => setToDate(e.target.value)}
                />
              </div>
              <Button onClick={handleSearchBySalesQuantity} disabled={loading} className="mt-4">
                {loading ? 'Searching...' : 'Search'}
              </Button>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>

      {/* Error Message */}
      {error && <ErrorMessage message={error} />}

      {/* Results */}
      {loading && <LoadingSpinner />}

      {!loading && products.length === 0 && !error && (
        <EmptyState
          title="No products found"
          description="Use the filters above to find products"
        />
      )}

      {!loading && products.length > 0 && (
        <Card>
          <CardHeader>
            <CardTitle>Results ({products.length})</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="overflow-x-auto">
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>ID</TableHead>
                    <TableHead>Name</TableHead>
                    <TableHead>Category</TableHead>
                    <TableHead>Price</TableHead>
                    <TableHead>Stock</TableHead>
                    <TableHead>Status</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {products.map((product) => (
                    <TableRow key={product.id}>
                      <TableCell className="font-mono text-xs">{product.id}</TableCell>
                      <TableCell className="font-medium">{product.name?.trim() || '-'}</TableCell>
                      <TableCell>{product.category?.trim() || '-'}</TableCell>
                      <TableCell>{formatCurrency(product.price)}</TableCell>
                      <TableCell>{product.stock}</TableCell>
                      <TableCell>
                        <span className={`px-2 py-1 rounded text-xs font-medium ${
                          product.status === 'active'
                            ? 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200'
                            : 'bg-gray-100 text-gray-800 dark:bg-gray-900 dark:text-gray-200'
                        }`}>
                          {product.status}
                        </span>
                      </TableCell>
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
