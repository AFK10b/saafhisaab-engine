import { useEffect, useState } from 'react'
import { useNavigate, useLocation } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import api from '../api/axios'


export default function OwnerDashboard() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()
  const location = useLocation() 
  const [activeShift, setActiveShift] = useState(null)
  const [openingCash, setOpeningCash] = useState('')
  const [closingCash, setClosingCash] = useState('')
  const [loading, setLoading] = useState(false)
  const [message, setMessage] = useState('')

useEffect(() => {
    api.get(`/shifts/active/${user.vendorId}`)
       .then(res => { if (res.status === 200) setActiveShift(res.data) })
       .catch(() => {})
}, [])

  const fetchActiveShift = async () => {
    try {
      const res = await api.get(`/shifts/active/${user.vendorId}`)
      setActiveShift(res.data)
    } catch {
      setActiveShift(null) // no active shift, show Open form
    }
  }
  
  const handleOpenShift = async () => {
    setLoading(true)
    try {
      const res = await api.post('/shifts/open', {
        vendorId: user.vendorId,
        openingCash: parseFloat(openingCash)
      })
      setActiveShift(res.data)
      setMessage('✅ Shift opened successfully')
    } catch {
      setMessage('❌ Failed to open shift')
    } finally {
      setLoading(false)
    }
  }

  const handleCloseShift = async () => {
    setLoading(true)
    try {
        const res = await api.post('/shifts/close', {
            shiftId: activeShift.id,
            closingCash: parseFloat(closingCash)
        })
        setActiveShift(null)
        navigate('/reconciliation', { state: { report: res.data } })  // ← navigate with data
    } catch {
        setMessage('❌ Failed to close shift')
    } finally {
        setLoading(false)
    }
}

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <div className="min-h-screen bg-gray-50">

      {/* Header */}
      <div className="bg-teal-700 text-white px-4 py-4 flex justify-between items-center">
        <div>
          <h1 className="text-xl font-bold">SaafHisaab</h1>
          <p className="text-teal-200 text-sm">Welcome, {user?.name}</p>
        </div>
        <button
          onClick={handleLogout}
          className="bg-teal-800 hover:bg-teal-900 px-4 py-2 rounded-lg text-sm"
        >
          Logout
        </button>
      </div>

      <div className="max-w-md mx-auto px-4 py-6 space-y-6">

        {/* Status Banner */}
        {message && (
          <div className="bg-white rounded-xl shadow-sm p-4 text-center text-sm font-medium text-gray-700">
            {message}
          </div>
        )}

        {/* No Active Shift — Open Shift Card */}
        {!activeShift && (
          <div className="bg-white rounded-2xl shadow-sm p-6 space-y-4">
            <h2 className="text-lg font-semibold text-gray-800">Open New Shift</h2>
            <div>
              <label className="block text-sm text-gray-600 mb-1">Opening Cash (₹)</label>
              <input
                type="number"
                value={openingCash}
                onChange={(e) => setOpeningCash(e.target.value)}
                placeholder="e.g. 500"
                className="w-full border border-gray-300 rounded-lg px-4 py-3 text-base focus:outline-none focus:ring-2 focus:ring-teal-500"
              />
            </div>
            <button
              onClick={handleOpenShift}
              disabled={!openingCash || loading}
              className="w-full bg-teal-700 hover:bg-teal-800 text-white font-semibold py-3 rounded-lg transition disabled:opacity-50"
            >
              {loading ? 'Opening...' : 'Open Shift'}
            </button>
          </div>
        )}

        {/* Active Shift — Close Shift Card */}
        {activeShift && (
          <div className="bg-white rounded-2xl shadow-sm p-6 space-y-4">
            <div className="flex justify-between items-center">
              <h2 className="text-lg font-semibold text-gray-800">Active Shift</h2>
              <span className="bg-green-100 text-green-700 text-xs font-medium px-3 py-1 rounded-full">
                OPEN
              </span>
            </div>
            <p className="text-sm text-gray-500">
              Opening Cash: <span className="font-semibold text-gray-800">₹{activeShift.openingCash}</span>
            </p>

            {/* Go to Sale Entry */}
            <button
              onClick={() => navigate('/sale', { state: { shiftId: activeShift.id } })}
              className="w-full bg-blue-600 hover:bg-blue-700 text-white font-semibold py-3 rounded-lg transition"
            >
              📋 Log a Sale
            </button>

            {/* Close Shift */}
            <div>
              <label className="block text-sm text-gray-600 mb-1">Closing Cash (₹)</label>
              <input
                type="number"
                value={closingCash}
                onChange={(e) => setClosingCash(e.target.value)}
                placeholder="e.g. 1200"
                className="w-full border border-gray-300 rounded-lg px-4 py-3 text-base focus:outline-none focus:ring-2 focus:ring-teal-500"
              />
            </div>
            <button
              onClick={handleCloseShift}
              disabled={!closingCash || loading}
              className="w-full bg-red-600 hover:bg-red-700 text-white font-semibold py-3 rounded-lg transition disabled:opacity-50"
            >
              {loading ? 'Closing...' : 'Close Shift & Reconcile'}
            </button>
          </div>
        )}

      </div>
    </div>
  )
}