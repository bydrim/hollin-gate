@import java.util.List
@import com.bydrim.hollingate.configs.GatewayConfig
@param List<GatewayConfig.Direction> directions

@template.layouts.default(content = @`
    <table>
        <tr>
            <th>type</th>
            <th>hosts</th>
            <th>path prefix</th>
            <th>target</th>
        </tr>
        @for(GatewayConfig.Direction dir : directions)
            <tr>
                <td>${dir.type()}</td>
                <td>${String.join("\n", dir.hosts())}</td>
                <td>${dir.pathPrefix()}</td>
                <td>${dir.target()}</td>
            </tr>
        @endfor
    </table>
`)
