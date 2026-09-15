@import java.util.List
@import com.bydrim.hollingate.entities.Tracker
@import com.bydrim.hollingate.entities.TrackerTrigger

@param Tracker tracker
@param List<TrackerTrigger> triggers

@template.layouts.default(content = @`
    <table>
        <tr>
            <th>id</th>
            <th>description</th>
            <th>create date</th>
            <th>last trigger date</th>
            <th>delete</th>
        </tr>
        <tr>
            <td>${tracker.getId()}</td>
            <td>${tracker.getDescription()}</td>
            <td>${tracker.getCreateDate() == null ? "-" : tracker.getCreateDate().toString()}</td>
            <td>${tracker.getLastTriggerDate() == null ? "-" : tracker.getLastTriggerDate().toString()}</td>
            <td>
                <button onclick="deleteTracker('${tracker.getId()}')">Delete ${tracker.getId()}</button>
            </td>
        </tr>
    </table>
    <br>
    <table>
        <tr>
            <th>trigger id</th>
            <th>url</th>
            <th>date</th>
        </tr>
        <tr>
            @for(TrackerTrigger trigger : triggers)
                <td>${trigger.getId()}</td>
                <td>${trigger.getTriggerUrl().toString()}</td>
                <td>${trigger.getTriggerDate().toString()}</td>
            @endfor
        </tr>
    </table>
    <script>
        async function deleteTracker(id) {
            const response = await fetch(document.URL, {
                method: 'DELETE'
            });
            if (response.ok) {
                window.location.replace('.');
            } else {
                console.log('error deleting tracker ' + id);
            }
        }
    </script>
`)
