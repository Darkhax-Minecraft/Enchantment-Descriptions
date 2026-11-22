//! Enchantment Descriptions for Horizon: appends localized enchantment descriptions to item tooltips.

use mod_api::text::TextStyle;
use mod_api::{self, ClientHost, Compound, Hooks, ItemTooltipEvent, Tag, Text};

const DARK_GRAY_RGBA: u32 = 0x5555_55FF;

/// Converts a namespaced enchantment id (e.g. `"minecraft:sharpness"`) to its lang key
/// (`"enchantment.minecraft.sharpness.desc"`).
fn enchantment_desc_key(id: &str) -> String {
    let (ns, path) = id.split_once(':').unwrap_or(("minecraft", id));
    format!("enchantment.{ns}.{path}.desc")
}

/// Looks up a key in a `Compound` without assuming `Borrow<str>` on `JavaString`.
fn compound_get<'a>(map: &'a Compound, key: &str) -> Option<&'a Tag> {
    map.iter()
        .find(|(k, _)| k.to_string().ok().as_deref() == Some(key))
        .map(|(_, v)| v)
}

/// Extracts enchantment ids from a `minecraft:enchantments` or `minecraft:stored_enchantments` compound.
fn collect_enchantment_ids(tag: &Tag, out: &mut Vec<String>) {
    let Tag::Compound(map) = tag else {
        return;
    };
    if let Some(Tag::Byte(0)) = compound_get(map, "show_in_tooltip") {
        return;
    }
    let levels: &Compound = match compound_get(map, "levels") {
        Some(Tag::Compound(inner)) => inner,
        _ => map,
    };
    for (key, val) in levels {
        let Ok(k) = key.to_string() else {
            continue;
        };
        if k == "show_in_tooltip" {
            continue;
        }
        if matches!(val, Tag::Int(_) | Tag::Short(_) | Tag::Byte(_)) {
            out.push(k);
        }
    }
}

#[derive(Default)]
struct EnchDescMod;

#[mod_api::horizon_mod]
impl Hooks for EnchDescMod {
    fn item_tooltip(&self, _host: &dyn ClientHost, event: &mut ItemTooltipEvent<'_>) {
        let Some(components) = event.stack.components() else {
            return;
        };
        let nbt = components.as_nbt();
        let mut ids = Vec::new();
        for component_key in ["minecraft:enchantments", "minecraft:stored_enchantments"] {
            if let Some(tag) = compound_get(nbt, component_key) {
                collect_enchantment_ids(tag, &mut ids);
            }
        }
        if ids.is_empty() {
            return;
        }
        let mut style = TextStyle::default();
        style.color = Some("dark_gray".to_string());
        for id in ids {
            let line = Text::translate(enchantment_desc_key(&id), Vec::new()).with_style(style.clone());
            event.lines.push((line, DARK_GRAY_RGBA));
        }
    }
}

#[cfg(test)]
mod tests {
    use super::enchantment_desc_key;

    #[test]
    fn builds_standard_enchdesc_lang_keys() {
        assert_eq!(
            enchantment_desc_key("minecraft:sharpness"),
            "enchantment.minecraft.sharpness.desc"
        );
        assert_eq!(
            enchantment_desc_key("unbreaking"),
            "enchantment.minecraft.unbreaking.desc"
        );
    }
}
