import android.util.Log
import com.example.victor_ai.data.network.RetrofitInstance
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import java.time.Instant


data class DiaryEntry(
    val account_id: String,
    val entry_text: String,
    val timestamp: String // Или `Long` — зависит от FastAPI
)

data class DiaryResponse(
    val status: String,
    val message: String? = null
)



