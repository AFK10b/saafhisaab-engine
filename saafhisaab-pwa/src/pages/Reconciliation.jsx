import { useLocation, useNavigate } from 'react-router-dom'

export default function Reconciliation() {
    const { state } = useLocation()
    const navigate = useNavigate()
    const report = state?.report

    if (!report) {
        navigate('/dashboard')
        return null
    }

    return (
        <div className="min-h-screen bg-gray-50">
            <div className="bg-teal-700 text-white px-4 py-4">
                <h1 className="text-xl font-bold">Shift Report</h1>
                <p className="text-teal-200 text-sm">Shift #{report.shiftId}</p>
            </div>
            <div className="max-w-md mx-auto px-4 py-6">
                <div className="bg-white rounded-2xl shadow-sm p-6 space-y-3">
                    <h2 className="text-lg font-semibold text-gray-800">📊 Reconciliation Summary</h2>

                    <div className="flex justify-between text-sm py-2 border-b border-gray-100">
                        <span className="text-gray-500">Opening Cash</span>
                        <span className="font-semibold">₹{report.openingCash}</span>
                    </div>
                    <div className="flex justify-between text-sm py-2 border-b border-gray-100">
                        <span className="text-gray-500">Total Sales</span>
                        <span className="font-semibold text-teal-700">₹{report.totalSales}</span>
                    </div>
                    <div className="flex justify-between text-sm py-2 border-b border-gray-100">
                        <span className="text-gray-500">Expected Cash</span>
                        <span className="font-semibold">₹{report.expectedCash}</span>
                    </div>
                    <div className="flex justify-between text-sm py-2 border-b border-gray-100">
                        <span className="text-gray-500">Actual Cash</span>
                        <span className="font-semibold">₹{report.actualCash}</span>
                    </div>
                    <div className="flex justify-between text-sm py-2 border-b border-gray-100">
                        <span className="text-gray-500">Discrepancy</span>
                        <span className={`font-bold ${report.discrepancy === 0 ? 'text-green-600' : 'text-red-600'}`}>
                            ₹{report.discrepancy}
                        </span>
                    </div>
                    <div className="flex justify-between text-sm py-2">
                        <span className="text-gray-500">Status</span>
                        <span className={`font-bold px-2 py-1 rounded-full text-xs ${
                            report.status === 'MATCHED' 
                                ? 'bg-green-100 text-green-700' 
                                : 'bg-red-100 text-red-700'
                        }`}>
                            {report.status}
                        </span>
                    </div>

                    <button
                        onClick={() => navigate('/dashboard')}
                        className="w-full bg-teal-700 hover:bg-teal-800 text-white font-semibold py-3 rounded-lg mt-4"
                    >
                        Back to Dashboard
                    </button>
                </div>
            </div>
        </div>
    )
}