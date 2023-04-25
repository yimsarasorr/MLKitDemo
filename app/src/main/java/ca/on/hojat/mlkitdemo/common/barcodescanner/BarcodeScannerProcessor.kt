package ca.on.hojat.mlkitdemo.common.barcodescanner

import android.content.Context
import android.util.Log
import ca.on.hojat.mlkitdemo.common.GraphicOverlay
import ca.on.hojat.mlkitdemo.common.VisionProcessorBase
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

/** Barcode Detector Demo.  */
class BarcodeScannerProcessor(context: Context) : VisionProcessorBase<List<Barcode>>(context) {

    // Note that if you know which format of barcode your app is dealing with, detection will be
    // faster to specify the supported barcode formats one by one, e.g.
    // BarcodeScannerOptions.Builder()
    //     .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
    //     .build();
    private val barcodeScanner: BarcodeScanner = BarcodeScanning.getClient()

    override fun stop() {
        super.stop()
        barcodeScanner.close()
    }

    override fun detectInImage(image: InputImage): Task<List<Barcode>> {
        return barcodeScanner.process(image)
    }

    override fun onSuccess(results: List<Barcode>, graphicOverlay: GraphicOverlay) {
        if (results.isEmpty()) {
            Log.v(MANUAL_TESTING_LOG, "No barcode has been detected")
        }
        for (i in results.indices) {
            val barcode = results[i]
            graphicOverlay.add(BarcodeGraphic(graphicOverlay, barcode))
            logExtrasForTesting(barcode)
        }
    }

    override fun onFailure(e: Exception) {
        Log.e(TAG, "Barcode detection failed $e")
    }

    companion object {
        private const val TAG = "BarcodeProcessor"

        private fun logExtrasForTesting(barcode: Barcode?) {
            if (barcode != null) {
                Log.v(
                    MANUAL_TESTING_LOG, String.format(
                        "Detected barcode's bounding box: %s",
                        barcode.boundingBox!!.flattenToString()
                    )
                )
                Log.v(
                    MANUAL_TESTING_LOG, String.format(
                        "Expected corner point size is 4, get %d", barcode.cornerPoints!!.size
                    )
                )
                for (point in barcode.cornerPoints!!) {
                    Log.v(
                        MANUAL_TESTING_LOG, String.format(
                            "Corner point is located at: x = %d, y = %d", point.x, point.y
                        )
                    )
                }
                Log.v(
                    MANUAL_TESTING_LOG, "barcode display value: " + barcode.displayValue
                )
                Log.v(
                    MANUAL_TESTING_LOG, "barcode raw value: " + barcode.rawValue
                )
                val dl = barcode.driverLicense
                if (dl != null) {
                    Log.v(
                        MANUAL_TESTING_LOG, "driver license city: " + dl.addressCity
                    )
                    Log.v(
                        MANUAL_TESTING_LOG, "driver license state: " + dl.addressState
                    )
                    Log.v(
                        MANUAL_TESTING_LOG, "driver license street: " + dl.addressStreet
                    )
                    Log.v(
                        MANUAL_TESTING_LOG, "driver license zip code: " + dl.addressZip
                    )
                    Log.v(
                        MANUAL_TESTING_LOG, "driver license birthday: " + dl.birthDate
                    )
                    Log.v(
                        MANUAL_TESTING_LOG, "driver license document type: " + dl.documentType
                    )
                    Log.v(
                        MANUAL_TESTING_LOG, "driver license expiry date: " + dl.expiryDate
                    )
                    Log.v(
                        MANUAL_TESTING_LOG, "driver license first name: " + dl.firstName
                    )
                    Log.v(
                        MANUAL_TESTING_LOG, "driver license middle name: " + dl.middleName
                    )
                    Log.v(
                        MANUAL_TESTING_LOG, "driver license last name: " + dl.lastName
                    )
                    Log.v(
                        MANUAL_TESTING_LOG, "driver license gender: " + dl.gender
                    )
                    Log.v(
                        MANUAL_TESTING_LOG, "driver license issue date: " + dl.issueDate
                    )
                    Log.v(
                        MANUAL_TESTING_LOG, "driver license issue country: " + dl.issuingCountry
                    )
                    Log.v(
                        MANUAL_TESTING_LOG, "driver license number: " + dl.licenseNumber
                    )
                }
            }
        }
    }
}
