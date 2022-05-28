package imgui;

import java.util.Objects;

public final class ImFloat {
    final float[] data = new float[]{0};

    public ImFloat() {
    }

    public ImFloat(final float value) {
        set(value);
    }

    public float get() {
        return this.data[0];
    }

    public void set(final float value) {
        this.data[0] = value;
    }

    @Override
    public String toString() {
        return String.valueOf(get());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ImFloat imFloat = (ImFloat) o;
        return data[0] == imFloat.data[0];
    }

    @Override
    public int hashCode() {
        return Objects.hash(data[0]);
    }
}
