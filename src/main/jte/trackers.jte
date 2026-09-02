@import java.util.List
@import com.bydrim.hollingate.entities.Tracker
@param List<Tracker> trackers

<!DOCTYPE html>
<html>
<body>
<ul>
    @for(Tracker tracker : trackers)
        <li>${tracker.toString()}</li>
    @endfor
</ul>
</body>
</html>
