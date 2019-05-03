package core.config;

public class DataConfig {
	public enum PlatformName {
		Android, iOS
	}

	public enum DeviceMode {
		iOS(0), Android(1), Genimotion(2),Simulator(3);

		private int value;

		private DeviceMode(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
}
