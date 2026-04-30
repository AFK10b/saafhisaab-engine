import { useState, useEffect } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import api from '../api/axios'

export default function SalePage() {
    const { state } = useLocation()
    const navigate = useNavigate()
    const { user } = useAuth()
    const shiftId = state?.shiftId

    const [menuItems, setMenuItems] = useState([])
    const [selectedItem, setSelectedItem] = useState(null)
    const [quantity, setQuantity] = useState(1)
    const [loading, setLoading] = useState(false)
    const [message, setMessage] = useState('')
    const [sales, setSales] = useState([])

    if (!shiftId) {
        navigate('/dashboard')
        return null
    }

    useEffect(() => {
        api.get(`/menu-items/vendor/${user.vendorId}`)
            .then(res => setMenuItems(res.data))
            .catch(() => setMessage('❌ Failed to load menu items'))
    }, [])

    const totalPrice = selectedItem ? selectedItem.price * quantity : 0

    const handleSubmit = async () => {
        setLoading(true)
        try {
            const res = await api.post('/sales/log', {
                shiftId,
                menuItemId: selectedItem.id,
                quantity: parseInt(quantity)
            })
            setSales(prev => [{ ...res.data, itemName: selectedItem.name }, ...prev])
            setMessage('✅ Sale logged!')
            setSelectedItem(null)
            setQuantity(1)
        } catch (err) {
            setMessage('❌ ' + (err.response?.data?.message || 'Failed to log sale'))
        } finally {
            setLoading(false)
            setTimeout(() => setMessage(''), 3000)
        }
    }

    const sessionTotal = sales.reduce((sum, s) => sum + (s.totalPrice || 0), 0)

    return (
        <div className="min-h-screen bg-gray-50">

            {/* Header */}
            <div className="bg-teal-700 text-white px-4 py-4 flex justify-between items-center">
                <div>
                    <h1 className="text-xl font-bold">Log a Sale</h1>
                    <p className="text-teal-200 text-sm">Shift #{shiftId}</p>
                </div>
                <button
                    onClick={() => navigate('/dashboard')}
                    className="bg-teal-800 hover:bg-teal-900 px-4 py-2 rounded-lg text-sm"
                >
                    ← Dashboard
                </button>
            </div>

            <div className="max-w-md mx-auto px-4 py-6 space-y-4">

                {message && (
                    <div className="bg-white rounded-xl shadow-sm p-3 text-center text-sm font-medium text-gray-700">
                        {message}
                    </div>
                )}

                {/* Menu Item Picker */}
                <div className="bg-white rounded-2xl shadow-sm p-6 space-y-4">
                    <h2 className="text-lg font-semibold text-gray-800">Select Item</h2>

                    <div className="grid grid-cols-2 gap-2">
                        {menuItems.map(item => (
                            <button
                                key={item.id}
                                onClick={() => setSelectedItem(item)}
                                className={`p-3 rounded-xl border-2 text-left transition ${
                                    selectedItem?.id === item.id
                                        ? 'border-teal-500 bg-teal-50'
                                        : 'border-gray-200 bg-white'
                                }`}
                            >
                                <p className="text-sm font-semibold text-gray-800">{item.name}</p>
                                <p className="text-xs text-teal-600 mt-1">₹{item.price}</p>
                            </button>
                        ))}
                    </div>

                    {menuItems.length === 0 && (
                        <p className="text-sm text-gray-400 text-center py-4">
                            No menu items found.{' '}
                            <span className="text-teal-600 cursor-pointer" onClick={() => navigate('/menu')}>
                                Add items →
                            </span>
                        </p>
                    )}
                </div>

                {/* Quantity + Submit */}
                {selectedItem && (
                    <div className="bg-white rounded-2xl shadow-sm p-6 space-y-4">
                        <div className="flex justify-between items-center">
                            <h2 className="text-lg font-semibold text-gray-800">{selectedItem.name}</h2>
                            <span className="text-sm text-gray-500">₹{selectedItem.price} each</span>
                        </div>

                        {/* Quantity Stepper */}
                        <div className="flex items-center gap-4">
                            <button
                                onClick={() => setQuantity(q => Math.max(1, q - 1))}
                                className="w-10 h-10 rounded-full bg-gray-100 hover:bg-gray-200 text-xl font-bold flex items-center justify-center"
                            >
                                −
                            </button>
                            <span className="text-2xl font-bold text-gray-800 w-8 text-center">{quantity}</span>
                            <button
                                onClick={() => setQuantity(q => q + 1)}
                                className="w-10 h-10 rounded-full bg-gray-100 hover:bg-gray-200 text-xl font-bold flex items-center justify-center"
                            >
                                +
                            </button>
                        </div>

                        <div className="bg-teal-50 rounded-lg px-4 py-3 flex justify-between items-center">
                            <span className="text-sm text-teal-700">Total</span>
                            <span className="text-lg font-bold text-teal-700">₹{totalPrice.toFixed(2)}</span>
                        </div>

                        <button
                            onClick={handleSubmit}
                            disabled={loading}
                            className="w-full bg-teal-700 hover:bg-teal-800 text-white font-semibold py-3 rounded-lg transition disabled:opacity-50"
                        >
                            {loading ? 'Logging...' : '✅ Log Sale'}
                        </button>
                    </div>
                )}

                {/* Session Log */}
                {sales.length > 0 && (
                    <div className="bg-white rounded-2xl shadow-sm p-6 space-y-3">
                        <div className="flex justify-between items-center">
                            <h2 className="text-lg font-semibold text-gray-800">This Session</h2>
                            <span className="text-sm font-bold text-teal-700">₹{sessionTotal.toFixed(2)}</span>
                        </div>
                        {sales.map((sale, i) => (
                            <div key={i} className="flex justify-between items-center py-2 border-b border-gray-100 last:border-0">
                                <div>
                                    <p className="text-sm font-medium text-gray-800">{sale.itemName}</p>
                                    <p className="text-xs text-gray-400">qty: {sale.quantity}</p>
                                </div>
                                <span className="text-sm font-semibold">₹{sale.totalPrice}</span>
                            </div>
                        ))}
                    </div>
                )}

            </div>
        </div>
    )
}