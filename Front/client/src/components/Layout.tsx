import { Link } from 'wouter';
import { Menu, X } from 'lucide-react';
import { useState } from 'react';
import { APP_LOGO, APP_TITLE } from '@/const';

interface LayoutProps {
  children: React.ReactNode;
}

export default function Layout({ children }: LayoutProps) {
  const [sidebarOpen, setSidebarOpen] = useState(true);

  const navItems = [
    { label: 'Home', href: '/' },
    { label: 'Users', href: '/users' },
    { label: 'Products', href: '/products' },
    { label: 'Orders', href: '/orders' },
    { label: 'Employees', href: '/employees' },
    { label: 'Dependents', href: '/dependents' },
    { label: 'Departments', href: '/departments' },
  ];

  return (
    <div className="flex h-screen bg-background">
      {/* Sidebar */}
      <aside
        className={`${
          sidebarOpen ? 'w-64' : 'w-0'
        } bg-card border-r border-border transition-all duration-300 overflow-hidden flex flex-col`}
      >
        <div className="p-6 border-b border-border">
          <div className="flex items-center gap-3">
            <img src={APP_LOGO} alt="Logo" className="w-8 h-8" />
            <h1 className="text-lg font-bold text-foreground">{APP_TITLE}</h1>
          </div>
        </div>

        <nav className="flex-1 overflow-y-auto p-4">
          <ul className="space-y-2">
            {navItems.map((item) => (
              <li key={item.href}>
                <Link href={item.href}>
                  <a className="block px-4 py-2 rounded-md text-foreground hover:bg-accent hover:text-accent-foreground transition-colors">
                    {item.label}
                  </a>
                </Link>
              </li>
            ))}
          </ul>
        </nav>

        <div className="p-4 border-t border-border text-xs text-muted-foreground">
          <p>API Frontend Dashboard</p>
          <p>Connected to localhost:8080</p>
        </div>
      </aside>

      {/* Main Content */}
      <div className="flex-1 flex flex-col overflow-hidden">
        {/* Header */}
        <header className="bg-card border-b border-border px-6 py-4 flex items-center justify-between">
          <button
            onClick={() => setSidebarOpen(!sidebarOpen)}
            className="p-2 hover:bg-accent rounded-md transition-colors"
          >
            {sidebarOpen ? (
              <X className="w-5 h-5" />
            ) : (
              <Menu className="w-5 h-5" />
            )}
          </button>
          <h2 className="text-xl font-semibold text-foreground">{APP_TITLE}</h2>
          <div className="w-10" />
        </header>

        {/* Page Content */}
        <main className="flex-1 overflow-y-auto bg-background">
          {children}
        </main>
      </div>
    </div>
  );
}
