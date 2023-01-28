package me.tustin.items.utils;

import org.bukkit.Material;
import org.checkerframework.checker.units.qual.A;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapUtils {

	public static Map<Material, Material> get(List<Pair> datas) {
		Map<Material, Material> map = new HashMap<>();
		for(Pair data : datas) {
			map.put(data.a, data.b);
		}
		return map;
	}

	public static class Pair {
		private Material a;
		private Material b;

		public Pair(Material a, Material b) {
			this.a = a;
			this.b = b;
		}
	}

}
