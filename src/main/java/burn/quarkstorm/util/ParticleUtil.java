package burn.quarkstorm.util;

import burn.quarkstorm.Main;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class ParticleUtil {

    private static final Set<String> VALID_EFFECTS = new HashSet<>();
    static {
        VALID_EFFECTS.add("ring");
        VALID_EFFECTS.add("heart");
        VALID_EFFECTS.add("sphere");
        VALID_EFFECTS.add("helix");
        VALID_EFFECTS.add("laser");
    }

    public void executeQuarkParticle(String particleEffect, Player player, double xOffset, double yOffset, double zOffset) {
        particleEffect = particleEffect.toLowerCase();

        if (VALID_EFFECTS.contains(particleEffect)) {
            switch (particleEffect) {
                case "ring":
                    generateRingParticle(player, xOffset, yOffset, zOffset);
                    break;
                case "heart":
                    generateHeartParticles(player, xOffset, yOffset, zOffset);
                    break;
                case "sphere":
                    generateSphereParticles(player, xOffset, yOffset, zOffset);
                    break;
                case "helix":
                    generateHelixParticles(player, xOffset, yOffset, zOffset);
                    break;
                case "laser":
                    iAmFiringMyLaserBoom(player, xOffset, yOffset, zOffset);
                    break;
                default:
                    throw new RuntimeException("Invalid ParticleName, this should not have been reached.");
            }
        } else {
            player.sendRawMessage("Der angegebene effekt existiert nicht. Versuche einen von diesen: " + Arrays.stream(getValidEffects().toArray()).toList());
        }
    }

    private void generateRingParticle(Player player, double xOffset, double yOffset, double zOffset) {
        Location playerLocation = player.getLocation();
        double maxRadius = 2.5;
        double growRate = 0.25;
        int particles = 100;

        new BukkitRunnable() {
            double currentRadius = 1.0;

            @Override
            public void run() {
                if (currentRadius > maxRadius) {
                    this.cancel();
                    return;
                }

                for (int i = 0; i < particles; i++) {
                    double angle = 2 * Math.PI * i / particles;
                    double x = playerLocation.getX() + currentRadius * Math.cos(angle) + xOffset;
                    double z = playerLocation.getZ() + currentRadius * Math.sin(angle) + zOffset;
                    Location particleLocation = new Location(player.getWorld(), x, playerLocation.getY() + yOffset, z);
                    player.getWorld().spawnParticle(Particle.FLAME, particleLocation, 1, 0, 0, 0, 0);
                }
                currentRadius += growRate;
            }
        }.runTaskTimer(Main.getPlugin(), 0, 1);
    }

    public void generateHeartParticles(Player player, double xOffset, double yOffset, double zOffset) {
        Location location = player.getLocation().add(xOffset, yOffset, zOffset);
        new BukkitRunnable() {
            double t = 0;

            @Override
            public void run() {
                //Ich hasse Mathe, Ich hoffe ihr seid happy.
                t += Math.PI / 16;
                for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / 16) {
                    double x = 0.5 * (16 * Math.sin(theta) * Math.sin(theta) * Math.sin(theta));
                    double y = 0.5 * (13 * Math.cos(theta) - 5 * Math.cos(2 * theta) - 2 * Math.cos(3 * theta) - Math.cos(4 * theta));
                    Location particleLocation = location.clone().add(x, y + 2, 0);
                    player.getWorld().spawnParticle(Particle.HEART, particleLocation, 0);
                }
                if (t > Math.PI * 16) {
                    this.cancel();
                }
            }
        }.runTaskTimer(Main.getPlugin(), 0, 2);
    }

    public void generateSphereParticles(Player player, double xOffset, double yOffset, double zOffset) {
        Location center = player.getLocation().add(xOffset, yOffset, zOffset);
        double radius = 2.5;
        new BukkitRunnable() {
            double phi = 0;

            @Override
            public void run() {
                phi += Math.PI / 10;
                for (double theta = 0; theta <= Math.PI; theta += Math.PI / 10) {
                    double x = radius * Math.sin(theta) * Math.cos(phi);
                    double y = radius * Math.sin(theta) * Math.sin(phi);
                    double z = radius * Math.cos(theta);
                    Location particleLocation = center.clone().add(x, y, z);
                    player.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, particleLocation, 0);
                }
                if (phi > Math.PI * 2) {
                    this.cancel();
                }
            }
        }.runTaskTimer(Main.getPlugin(), 0, 2);
    }

    public void generateHelixParticles(Player player, double xOffset, double yOffset, double zOffset) {
        Location baseLocation = player.getLocation().add(xOffset, yOffset, zOffset);
        new BukkitRunnable() {
            double t = 0;

            @Override
            public void run() {
                t += Math.PI / 16;
                double y = t;
                if (y > 5) {
                    this.cancel();
                    return;
                }

                // Front
                double xFront = 1 * Math.cos(t);
                double zFront = 1 * Math.sin(t);
                Location frontLocation = baseLocation.clone().add(xFront, y, zFront);
                player.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, frontLocation, 0);

                // Back
                double xBack = 1 * Math.cos(t + Math.PI);
                double zBack = 1 * Math.sin(t + Math.PI);
                Location backLocation = baseLocation.clone().add(xBack, y, zBack);
                player.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, backLocation, 0);
            }
        }.runTaskTimer(Main.getPlugin(), 0, 2);
    }

    public void iAmFiringMyLaserBoom(Player player, double xOffset, double yOffset, double zOffset) {
        Location baseLocation = player.getLocation().add(xOffset, yOffset + 1.5, zOffset); // Height where player's hands are
        new BukkitRunnable() {
            int counter = 0;

            @Override
            public void run() {
                if (counter < 20) {
                    for (int i = 0; i < 10; i++) {
                        double angle = Math.random() * 2 * Math.PI;
                        double x = Math.cos(angle) * 0.5;
                        double z = Math.sin(angle) * 0.5;
                        Location particleLocation = baseLocation.clone().add(x, 0, z);
                        player.getWorld().spawnParticle(Particle.LARGE_SMOKE, particleLocation, 0);
                    }
                } else {
                    Vector direction = player.getLocation().getDirection().normalize();
                    for (int i = 0; i < 40; i++) {
                        Location particleLocation = baseLocation.clone().add(direction.clone().multiply(i * 0.25));
                        for (double x = -0.25; x <= 0.25; x += 0.1) {
                            for (double y = -0.25; y <= 0.25; y += 0.1) {
                                Location thickParticleLocation = particleLocation.clone().add(x, y, 0);
                                player.getWorld().spawnParticle(Particle.FLAME, thickParticleLocation, 0);
                            }
                        }
                    }
                    this.cancel();
                }
                counter++;
            }
        }.runTaskTimer(Main.getPlugin(), 0, 1);
    }

    public static Set<String> getValidEffects() {
        return VALID_EFFECTS;
    }
}


