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
            <input type="text" id="description-input" name="description" required/>
        </label>
        <button>Create</button>
    </fieldset>
</form>
<table>
    <tr>
        <th>id</th>
        <th>description</th>
        <th>create date</th>
        <th>last trigger date</th>
        <th>delete</th>
    </tr>
    @for(Tracker tracker : trackers)
        <tr>
            <td>${tracker.getId()}</td>
            <td>${tracker.getDescription()}</td>
            <td>${tracker.getCreateDate() == null ? "-" : tracker.getCreateDate().toString()}</td>
            <td>${tracker.getLastTriggerDate() == null ? "-" : tracker.getLastTriggerDate().toString()}</td>
            <td>
                <button onclick="deleteTracker('${tracker.getId()}')">Delete</button>
            </td>
        </tr>
    @endfor
</table>
<script>
    async function deleteTracker(id) {
        const response = await fetch(document.URL + '/' + id, {
            method: 'DELETE'
        });
        if (response.ok) {
            window.location.reload();
        } else {
            console.log('error deleting tracker ' + id);
        }
    }
</script>
</body>
</html>
