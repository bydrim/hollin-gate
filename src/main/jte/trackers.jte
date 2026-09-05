@import java.util.List
@import com.bydrim.hollingate.entities.Tracker
@param List<Tracker> trackers

<!DOCTYPE html>
<html>
<body>
<form method="post">
    <fieldset>
        <legend>Create new Tracker</legend>
        <label>
            Tracker Description:
            <input type="text" id="description-input" name="description" required />
        </label>
        <button>Create</button>
    </fieldset>
</form>
<ul>
    @for(Tracker tracker : trackers)
        <li>${tracker.toString()}</li>
    @endfor
</ul>
</body>
</html>
