package org.engine.Rendering.Assets.Sound;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryStack.stackPop;
import static org.lwjgl.system.libc.LibCStdlib.free;

public class Sound {
    private int bufferId;
    private int sourceId;//источник звука
    private String filepath;

    private boolean isPlaying = false;

    public Sound(String filePath, boolean loops){
        this.filepath = filePath;

        //выделение памяти под хранение информации о звуке
        stackPush();
        IntBuffer channelsBuffer = stackMallocInt(1);
        stackPush();
        IntBuffer sampleRateBuffer = stackMallocInt(1);//частота дискретизации

        ShortBuffer rawAudioBuffer = stb_vorbis_decode_filename(filePath, channelsBuffer, sampleRateBuffer);
        if(rawAudioBuffer == null){
            System.out.println("Could not load sound '" + filePath + "'");
            stackPop();
            stackPop();
            return;
        }

        //получение информации из буфферов
        int channels = channelsBuffer.get();
        int sampleRate = sampleRateBuffer.get();

        //освобождение
        stackPop();
        stackPop();

        //формат стерео или моно
        int format = -1;
        if(channels == 1){
            format = AL_FORMAT_MONO16;
        }else if(channels == 2){
            format = AL_FORMAT_STEREO16;
        }


        bufferId = alGenBuffers();
        alBufferData(bufferId, format, rawAudioBuffer, sampleRate);

        //генерация источника звука
        sourceId = alGenSources();

        alSourcei(sourceId, AL_BUFFER, bufferId); //какая звукавая дорожка из буффера
        alSourcei(sourceId, AL_LOOPING, loops ? 1 : 0);//зацикливание на дорожке
        alSourcei(sourceId, AL_POSITION, 0);//положение звука на дорожке
        alSourcef(sourceId, AL_GAIN, 0.3f);//усиление звука  на дорожке

        // освобождение сырого звукового буффера
        free(rawAudioBuffer);
    }

    public void delete() {//освобождение памяти
        alDeleteSources(sourceId);
        alDeleteBuffers(bufferId);
    }

    public void play() {
        int state = alGetSourcei(sourceId, AL_SOURCE_STATE);//получение статуса о звуке
        if (state == AL_STOPPED) {
            isPlaying = false;
            alSourcei(sourceId, AL_POSITION, 0);//установка на начале звуковой дорожки
        }

        if (!isPlaying) {
            alSourcePlay(sourceId);//воспроизведение звуковой дорожки
            isPlaying = true;
        }
    }

    public void stop() {
        if (isPlaying) {
            alSourceStop(sourceId);//остановка звуковой дорожки
            isPlaying = false;
        }
    }

    public String getFilepath() {
        return this.filepath;
    }

    public boolean isPlaying() {//получение информации о том, производится звук или нет
        int state = alGetSourcei(sourceId, AL_SOURCE_STATE);
        if (state == AL_STOPPED) {
            isPlaying = false;
        }
        return isPlaying;
    }
}
