@import java.util.List
@import com.bydrim.hollingate.configs.GatewayConfig
@param List<GatewayConfig.Direction> directions

<!DOCTYPE html>
<html>
<body>
<p>Hello World!</p>
<ul>
    @for(GatewayConfig.Direction dir : directions)
        <li>${String.join(", ", dir.hosts())} - ${dir.target()}</li>
    @endfor
</ul>
</body>
</html>