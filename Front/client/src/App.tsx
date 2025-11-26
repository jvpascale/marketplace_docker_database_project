import { Toaster } from "@/components/ui/sonner";
import { TooltipProvider } from "@/components/ui/tooltip";
import NotFound from "@/pages/NotFound";
import { Route, Switch } from "wouter";
import ErrorBoundary from "./components/ErrorBoundary";
import { ThemeProvider } from "./contexts/ThemeContext";
import Home from "./pages/Home";
import Layout from "./components/Layout";
import UsersPage from "./pages/UsersPage";
import ProductsPage from "./pages/ProductsPage";
import OrdersPage from "./pages/OrdersPage";
import EmployeesPage from "./pages/EmployeesPage";
import DependentsPage from "./pages/DependentsPage";
import DepartmentsPage from "./pages/DepartmentsPage";

function Router() {
  return (
    <Layout>
      <Switch>
        <Route path={"/"} component={Home} />
        <Route path={"/users"} component={UsersPage} />
        <Route path={"/products"} component={ProductsPage} />
        <Route path={"/orders"} component={OrdersPage} />
        <Route path={"/employees"} component={EmployeesPage} />
        <Route path={"/dependents"} component={DependentsPage} />
        <Route path={"/departments"} component={DepartmentsPage} />
        <Route path={"/404"} component={NotFound} />
        {/* Final fallback route */}
        <Route component={NotFound} />
      </Switch>
    </Layout>
  );
}

function App() {
  return (
    <ErrorBoundary>
      <ThemeProvider defaultTheme="light">
        <TooltipProvider>
          <Toaster />
          <Router />
        </TooltipProvider>
      </ThemeProvider>
    </ErrorBoundary>
  );
}

export default App;
