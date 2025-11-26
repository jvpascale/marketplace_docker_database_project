import { Link } from 'wouter';
import { useState } from 'react';
import { Users, Package, ShoppingCart, Briefcase, Users2, Building2, Database } from 'lucide-react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { databaseAPI } from '@/lib/api';
import { toast } from 'sonner';

export default function Home() {
  const [populatingDb, setPopulatingDb] = useState(false);
  const handlePopulateDatabase = async () => {
    setPopulatingDb(true);
    try {
      await databaseAPI.populateDatabase();
      toast.success('Database populated successfully!');
    } catch (err: any) {
      toast.error(err.response?.data?.message || 'Failed to populate database');
    } finally {
      setPopulatingDb(false);
    }
  };

  const entities = [
    {
      title: 'Users',
      description: 'Search and filter users by various criteria',
      icon: Users,
      href: '/users',
      color: 'bg-blue-50 dark:bg-blue-950',
      iconColor: 'text-blue-600 dark:text-blue-400',
    },
    {
      title: 'Products',
      description: 'Browse and filter products by seller, price, and sales',
      icon: Package,
      href: '/products',
      color: 'bg-green-50 dark:bg-green-950',
      iconColor: 'text-green-600 dark:text-green-400',
    },
    {
      title: 'Orders',
      description: 'View and filter orders by multiple criteria',
      icon: ShoppingCart,
      href: '/orders',
      color: 'bg-purple-50 dark:bg-purple-950',
      iconColor: 'text-purple-600 dark:text-purple-400',
    },
    {
      title: 'Employees',
      description: 'Manage and filter employees by department and supervisor',
      icon: Briefcase,
      href: '/employees',
      color: 'bg-orange-50 dark:bg-orange-950',
      iconColor: 'text-orange-600 dark:text-orange-400',
    },
    {
      title: 'Dependents',
      description: 'View dependents and family information',
      icon: Users2,
      href: '/dependents',
      color: 'bg-pink-50 dark:bg-pink-950',
      iconColor: 'text-pink-600 dark:text-pink-400',
    },
    {
      title: 'Departments',
      description: 'Explore departments and their operations',
      icon: Building2,
      href: '/departments',
      color: 'bg-teal-50 dark:bg-teal-950',
      iconColor: 'text-teal-600 dark:text-teal-400',
    },
  ];

  return (
    <div className="min-h-screen bg-background">
      {/* Hero Section */}
      <section className="px-6 py-12 md:py-20 border-b border-border">
        <div className="max-w-4xl mx-auto">
          <h1 className="text-4xl md:text-5xl font-bold text-foreground mb-4">
            API Frontend Dashboard
          </h1>
          <p className="text-lg text-muted-foreground mb-6">
            A clean and modern interface to explore and filter data from your API. 
            Connected to <code className="bg-muted px-2 py-1 rounded text-sm">localhost:8080</code>
          </p>
          <div className="flex flex-wrap gap-4 mb-6">
            <div className="text-sm text-muted-foreground">
              ✓ Real-time data from API
            </div>
            <div className="text-sm text-muted-foreground">
              ✓ Advanced filtering
            </div>
            <div className="text-sm text-muted-foreground">
              ✓ Responsive design
            </div>
          </div>
          <Button 
            onClick={handlePopulateDatabase} 
            disabled={populatingDb}
            className="gap-2"
          >
            <Database className="w-4 h-4" />
            {populatingDb ? 'Populating Database...' : 'Populate Database'}
          </Button>
        </div>
      </section>

      {/* Entity Cards */}
      <section className="px-6 py-12 md:py-20">
        <div className="max-w-6xl mx-auto">
          <h2 className="text-2xl font-bold text-foreground mb-12">Explore Entities</h2>
          
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {entities.map((entity) => {
              const IconComponent = entity.icon;
              return (
                <Link key={entity.href} href={entity.href}>
                  <a className="block h-full">
                    <Card className="h-full hover:shadow-lg hover:border-primary/50 transition-all duration-300 cursor-pointer">
                      <CardHeader>
                        <div className={`w-12 h-12 rounded-lg ${entity.color} flex items-center justify-center mb-4`}>
                          <IconComponent className={`w-6 h-6 ${entity.iconColor}`} />
                        </div>
                        <CardTitle className="text-foreground">{entity.title}</CardTitle>
                        <CardDescription>{entity.description}</CardDescription>
                      </CardHeader>
                      <CardContent>
                        <p className="text-xs text-muted-foreground">
                          Click to explore {entity.title.toLowerCase()} →
                        </p>
                      </CardContent>
                    </Card>
                  </a>
                </Link>
              );
            })}
          </div>
        </div>
      </section>

      {/* Info Section */}
      <section className="px-6 py-12 bg-muted/50 border-t border-border">
        <div className="max-w-4xl mx-auto">
          <h2 className="text-2xl font-bold text-foreground mb-6">How to Use</h2>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            <div>
              <h3 className="font-semibold text-foreground mb-2">1. Select an Entity</h3>
              <p className="text-sm text-muted-foreground">
                Choose from the entity cards above or use the sidebar navigation to access different data types.
              </p>
            </div>
            <div>
              <h3 className="font-semibold text-foreground mb-2">2. Apply Filters</h3>
              <p className="text-sm text-muted-foreground">
                Use the filtering forms to narrow down results based on your search criteria.
              </p>
            </div>
            <div>
              <h3 className="font-semibold text-foreground mb-2">3. View Results</h3>
              <p className="text-sm text-muted-foreground">
                Results are displayed in a clean table format with all relevant information.
              </p>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}
