@import java.util.List
@import com.bydrim.hollingate.configs.GatewayConfig
@param List<GatewayConfig.DirectionItem> directions

<!DOCTYPE html>
<html>
<body>
<p>Hello World!</p>
<ul>
    @for(GatewayConfig.DirectionItem dir : directions)
        <li>${dir.getFrom()} - ${dir.getTo()}</li>
    @endfor
</ul>
</body>
</html>