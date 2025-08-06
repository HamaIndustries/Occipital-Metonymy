package symbolics.division.occmy.client.view;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import symbolics.division.occmy.client.OCCMYClient;
import symbolics.division.occmy.net.C2SProjectionPayload;

public class CProjectionView {

    public static void open(World world, PlayerEntity player, @Nullable BlockPos pos) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (player == null) {
            player = client.player;
            if (!player.getBlockPos().isWithinDistance(pos, 4)) return;
        }

        BlockPos center = pos;
        if (center == null) {
            if (client.crosshairTarget instanceof BlockHitResult hit) {
                center = hit.getBlockPos();
            } else {
                return;
            }
        }

        Vec3d relative = player.getPos().subtract(center.toCenterPos());

        if (world.getBlockState(center).isAir()) return;

        int cx = center.getX();
        int cy = center.getY();
        int cz = center.getZ();

        BlockPos.Mutable bp = new BlockPos.Mutable();

        int dx = 20;
        int dy = 20;
        int dz = 20;

        BlockState[][][] area = new BlockState[dx * 2 + 1][dy * 2 + 1][dz * 2 + 1];

        for (int i = -dx; i <= dx; i++) {
            for (int j = -dx; j <= dy; j++) {
                for (int k = -dx; k <= dz; k++) {
                    bp.set(cx + i, cy + j, cz + k);
                    area[i + dx][j + dx][k + dx] = world.getBlockState(bp);
                }
            }
        }

        int sx = 2;
        int sy = 2;
        int sz = 2;
        int threshold = (sx * 2 + 1) * (sy * 2 + 1) * (sz * 2 + 1) / 2;
        BlockState[][][] sample = new BlockState[sx * 2 + 1][sy * 2 + 1][sz * 2 + 1];
        for (int i = -sx; i <= sx; i++) {
            for (int j = -sy; j <= sy; j++) {
                for (int k = -sz; k <= sz; k++) {
                    sample[i + sx][j + sx][k + sx] = area[dx + i][dy + j][dz + k];
                }
            }
        }

        int[] best = new int[3];
        int score = 0;
        int d = 0;

        for (int i = 0; i < dx * 2 + 1; i++) {
            for (int j = 0; j < dy * 2 + 1; j++) {
                for (int k = 0; k < dz * 2 + 1; k++) {
                    int c = 0;
                    for (int l = -sx; l <= sx; l++) {
                        for (int m = -sy; m <= sy; m++) {
                            for (int n = -sz; n <= sz; n++) {
                                int ox = i + l;
                                int oy = j + m;
                                int oz = k + n;
                                if (ox > 0 && oy > 0 && oz > 0 && ox < dx * 2 + 1 && oy < 2 * dy + 1 && oz < 2 * dz + 1
                                        && area[ox][oy][oz] == sample[l + sx][m + sy][n + sz]) {
                                    c++;
                                }
                            }
                        }
                    }
                    if (c != 0 && c >= score && c >= threshold) {
                        int ax = i - dx;
                        int ay = j - dy;
                        int az = k - dz;
                        int dist = ax * ax + ay * ay + az * az;
                        if ((ax == 0 && ay == 0 && az == 0) || (c == score && dist < d)) continue;
                        d = dist;
                        best = new int[]{cx + ax, cy + ay, cz + az};
                        score = c;
                    }
                }
            }
        }

        if (score > 0) {
            OCCMYClient.AFFAIRS.enableFor(Perspectives.OBSCURED, 20);
            final Vec3d p = new BlockPos(best[0], best[1], best[2]).toCenterPos().add(relative);
            ClientPlayNetworking.send(new C2SProjectionPayload(p));
        }

    }
}
