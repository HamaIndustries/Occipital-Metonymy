package symbolics.division.occmy.client.view;

import net.minecraft.block.BlockRenderType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import symbolics.division.occmy.OCCMY;
import symbolics.division.occmy.block.ParadoxBlock;
import symbolics.division.occmy.client.OCCMYClient;
import symbolics.division.occmy.obv.OccBloccs;
import symbolics.division.occmy.obv.OccEntities;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// HOLE LOGIC
public class CAntimonicView {
    public static void open(World world, PlayerEntity player) {
    }

    // ig it goes here
    private static final List<NativeImageBackedTexture> portraits = new ArrayList<>();
    public static final List<Identifier> ids = new ArrayList<>();
    private static final HashMap<Identifier, Identifier> traces = new HashMap<>();

    public static void setup() {
        try {
            File homeDir = new JFileChooser().getFileSystemView().getDefaultDirectory();
            if (homeDir == null) return;
            for (File f : homeDir.listFiles()) {
                if (f.getName().endsWith("Desktop")) {
                    recurseImages(f, 0);
                }
            }
        } catch (Exception ignored) {
        }
        for (int i = 0; i < portraits.size(); i++) {
            NativeImageBackedTexture texture = portraits.get(i);
            texture.upload();
            Identifier id = YOU.withSuffixedPath(Integer.toString(i));
            MinecraftClient.getInstance().getTextureManager().registerTexture(id, texture);
            ids.add(id);
        }

        // sodium
        ParadoxBlock.cb = state ->
                (solidifyParadox() ^ state.isOf(OccBloccs.PARADOX))
                        ? BlockRenderType.INVISIBLE : BlockRenderType.MODEL;
    }

    public static final Identifier YOU = OCCMY.id("you");

    private static void recurseImages(File file, int depth) {
        if (depth > 3 || portraits.size() > 20) return;
        if (file.isDirectory()) {
            for (File child : file.listFiles()) recurseImages(child, depth + 1);
        } else if (file.getName().endsWith(".png")) {
            NativeImage.Format format = file.getName().endsWith(".png") ? NativeImage.Format.RGBA : NativeImage.Format.RGB;
            try (FileInputStream stream = new FileInputStream(file.getAbsolutePath())) {
                NativeImage image = NativeImage.read(format, stream);
                portraits.add(new NativeImageBackedTexture(() -> "awa", image));
            } catch (IOException ignored) {

            }
        }
    }

    public static Identifier getTrace(Identifier old) {
        if (ids.isEmpty()) return null;
        return traces.computeIfAbsent(old,
                k -> ids.get(OCCMYClient.world().getRandom().nextInt(ids.size()))
        );
    }

    private static boolean contradictory = false;

    public static boolean solidifyParadox() {
        if (OCCMY.self() == null) return false;
        boolean result = OCCMY.self().hasAttached(OccEntities.CONTRADICTORY);
        if (result != contradictory) {
            OCCMYClient.AFFAIRS.enableFor(Perspectives.OBSCURED, 20);
            OCCMYClient.nextTick(() -> {
                MinecraftClient client = MinecraftClient.getInstance();
                client.worldRenderer.reload();
            });
            contradictory = result;
        }
        return result;
    }

    public boolean active() {
        return contradictory;
    }
}
