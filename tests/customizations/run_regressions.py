#!/usr/bin/env python3
"""Compile and exercise actual dock mapping sources without the Android SDK.

Minimal stubs supply only Android model constants/fields; all mapping logic
under test comes from the production Java sources, not a reimplementation.
"""
from pathlib import Path
import subprocess
import tempfile

ROOT = Path(__file__).resolve().parents[2]
with tempfile.TemporaryDirectory(prefix="lawnchair-dock-test-") as directory:
    temp = Path(directory)
    stubs = {
        "com/android/launcher3/LauncherSettings.java": """
package com.android.launcher3;
public class LauncherSettings {
    public static class Favorites {
        public static final int CONTAINER_DESKTOP = -100;
        public static final int CONTAINER_HOTSEAT = -101;
    }
}
""",
        "com/android/launcher3/model/data/ItemInfo.java": """
package com.android.launcher3.model.data;
public class ItemInfo { public int cellX, cellY, screenId, container; }
""",
    }
    for name, content in stubs.items():
        target = temp / name
        target.parent.mkdir(parents=True, exist_ok=True)
        target.write_text(content)
    sources = [str(temp / name) for name in stubs] + [
        str(ROOT / "lawnchair/src/app/lawnchair/hotseat/DockGrid.java"),
        str(ROOT / "src/com/android/launcher3/celllayout/CellPosMapper.java"),
        str(ROOT / "tests/customizations/DockGridRegression.java"),
    ]
    subprocess.run(["javac", "-d", str(temp), *sources], check=True)
    subprocess.run(["java", "-cp", str(temp), "customizations.DockGridRegression"], check=True)
