import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { AuthProvider } from './context/AuthContext'
import ProtectedRoute from './components/ProtectedRoute'
import Login from './pages/Login'
import OwnerDashboard from './pages/OwnerDashboard'
import SaleEntry from './pages/SaleEntry'
import Reconciliation from './pages/Reconciliation' 

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/dashboard" element={
            <ProtectedRoute><OwnerDashboard /></ProtectedRoute>
          } />
          <Route path="/sale" element={
            <ProtectedRoute><SaleEntry /></ProtectedRoute>
          } />
          <Route path="*" element={<Navigate to="/login" replace />} />
		  <Route path="/reconciliation" element={<Reconciliation />} />
		  <Route path="/sale" element={<SaleEntry />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  )
}