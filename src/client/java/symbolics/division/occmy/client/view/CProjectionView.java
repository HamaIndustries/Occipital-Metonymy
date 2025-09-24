package symbolics.division.occmy.client.view;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import symbolics.division.occmy.client.OCCMYClient;
import symbolics.division.occmy.ent.ProjectionEntity;
import symbolics.division.occmy.item.Thetiscope;
import symbolics.division.occmy.net.C2SProjectionPayload;
import symbolics.division.occmy.net.S2CCaptureImagePayload;
import symbolics.division.occmy.obv.OccEntities;
import symbolics.division.occmy.view.Views;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CProjectionView {

    public static void open(World world, PlayerEntity player, @Nullable BlockPos pos) {
        boolean indirect = player == null;
        MinecraftClient client = MinecraftClient.getInstance();
        if (player == null) {
            player = client.player;
            int protection = player.getAttachedOrSet(OccEntities.PROJECTION_PROTECTION, 0);
            if (!player.getBlockPos().isWithinDistance(pos, 4) || protection > 0) return;
            player.setAttached(OccEntities.PROJECTION_PROTECTION, 100);
        }

        if (player.isSpectator() || player.hasVehicle()) return;

        BlockPos center = pos;
        if (center == null) {
            HitResult hit = client.crosshairTarget;
            if (hit == null || hit instanceof BlockHitResult bhr && world.getBlockState(bhr.getBlockPos()).isAir()) {
                hit = player.raycast(4, 0, false);
            }
            if (hit instanceof BlockHitResult bhr) { // block
                BlockState h = world.getBlockState(((BlockHitResult) hit).getBlockPos());
                center = bhr.getBlockPos();
            } else if (hit != null) { //ent
                center = BlockPos.ofFloored(client.crosshairTarget.getPos().add(player.getRotationVecClient().multiply(0.5)));
            } else {
                return;
            }
        }


        BlockState centerState = world.getBlockState(center);
        if (centerState.isAir() || (!indirect && restricted(world, player, centerState))) return;

        OCCMYClient.AFFAIRS.enableFor(Perspectives.OBSCURED, 20);
        final PlayerEntity subject = player;
        final BlockPos object = center;
        OCCMYClient.nextTick(() -> project(world, subject, object));
    }

    private static void project(World world, PlayerEntity player, BlockPos center) {
        Vec3d relative = player.getPos().subtract(center.toCenterPos());

        int cx = center.getX();
        int cy = center.getY();
        int cz = center.getZ();

        BlockPos.Mutable bp = new BlockPos.Mutable();

        int r = 40;
        int dx = r;
        int dy = r;
        int dz = r;

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
        int threshold = 0;
        BlockState[][][] sample = new BlockState[sx * 2 + 1][sy * 2 + 1][sz * 2 + 1];
        for (int i = -sx; i <= sx; i++) {
            for (int j = -sy; j <= sy; j++) {
                for (int k = -sz; k <= sz; k++) {
                    sample[i + sx][j + sx][k + sx] = area[dx + i][dy + j][dz + k];
                    BlockState state = sample[i + sx][j + sx][k + sx];
                    if (!state.isAir()) {
                        threshold++;
                    }
                }
            }
        }
        threshold /= 2;

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
                                        && area[ox][oy][oz] == sample[l + sx][m + sy][n + sz] && !area[ox][oy][oz].isAir()) {
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

        if (score >= threshold) {
            final Vec3d p = new BlockPos(best[0], best[1], best[2]).toCenterPos().add(relative);
            if (!world.isBlockSpaceEmpty(player, player.getBoundingBox().offset(p.subtract(player.getPos())))) {
                player.playSound(SoundEvents.ITEM_SHIELD_BLOCK.value(), 0.8f, 1.2f);
                return;
            }
            if (CExteriorityView.active()) {
                player.setPosition(p);
            } else {
                ClientPlayNetworking.send(new C2SProjectionPayload(p));
            }
        }
    }

    private static final String our_promised_secret = "!\u0012G�\u001C\u0017\u001A��X�\u0013�\ba�E�4\bUŝ��W\u0017\u007F�Jw";

    public static boolean introspect(String v) {
        if (true) return true;
        if (!v.startsWith(".wit") || v.length() < 10) return true;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(v.getBytes());
            String hash = new String(messageDigest.digest());
            if (hash.equals(our_promised_secret)) {
                Thetiscope.special = () -> {
                    PlayerEntity player = OCCMYClient.player();
                    if (player != null) {
                        HitResult result = player.raycast(30, 0, false);
                        if (result instanceof BlockHitResult hitResult && hitResult.getType() != HitResult.Type.MISS) {
                            Vec3d pos = hitResult.getBlockPos().add(0, 1, 0).toCenterPos();
                            ClientPlayNetworking.send(new C2SProjectionPayload(pos));
                        }
                    }
                };
            }
        } catch (NoSuchAlgorithmException e) {

        }
        return false;
    }

    public static boolean restricted(World world, PlayerEntity player, BlockState state) {
        return false;
    }

    public static void handleCaptureImage(S2CCaptureImagePayload payload, ClientPlayNetworking.Context context) {
        ClientWorld world = context.client().world;
        Entity subject = world.getEntity(payload.subject());
        PlayerEntity self = OCCMYClient.player();
        if (self != null && subject != null && Views.matryoshka(self, subject::equals)) {
            OCCMYClient.AFFAIRS.enableFor(Perspectives.OBSCURED, 20);
        }
        if (subject != null) {
            subject.setAttached(OccEntities.PROJECTING, true);
            subject.setAttached(OccEntities.OBSCURED, true);
        }
        spawnImage(world, payload.from());
        spawnImage(world, payload.to());
    }

    private static void spawnImage(ClientWorld world, Vec3d pos) {
        ProjectionEntity proj = OccEntities.PROJECTION.create(world, SpawnReason.LOAD);
        world.addEntity(proj);
        proj.setPosition(pos);
        proj.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, MinecraftClient.getInstance().player.getEyePos());
    }
}
