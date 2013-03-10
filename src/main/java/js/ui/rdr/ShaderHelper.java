package js.ui.rdr;

import com.jogamp.opengl.util.glsl.ShaderUtil;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.media.opengl.GL4bc;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * Created by IntelliJ IDEA.
 * User: jgg
 * Date: 05.11.11
 * Time: 12:00
 * To change this template use File | Settings | File Templates.
 */
public class ShaderHelper {

    private static final Logger LOG = LoggerFactory.getLogger(ShaderHelper.class);

    private final GL4bc gl;

    public static enum ShaderType {
        VertextShader("vert", GL4bc.GL_VERTEX_SHADER), FragmentShader("frag", GL4bc.GL_FRAGMENT_SHADER);

        String extension;
        int glType;

        ShaderType(String extension, int glType) {
            this.extension = extension;
            this.glType = glType;
        }
    }

    public ShaderHelper(GL4bc gl) {
        this.gl = gl;


    }


//    LOG.info("Build shader ...");
//        int program = createShaderProgram(gl,
//                //createShader(gl, "simple", ShaderType.FragmentShader)
//                createShader(gl, "blur_h", ShaderType.VertextShader),
//                createShader(gl, "blur_h", ShaderType.FragmentShader)
//                //createShader(gl, "glitter", ShaderType.VertextShader)
//                //, createShader(gl, "glitter", ShaderType.FragmentShader)
//        );

//        LOG.info("Use program {}", program);
//        gl.glUseProgram(program);

    public int createShaderProgram(int... shaders) {
        int program = gl.glCreateProgram();
        for (int shader : shaders) {
            gl.glAttachShader(program, shader);
        }
        gl.glLinkProgram(program);
        gl.glValidateProgram(program);

        checkItem(program, "Unable to create program");
        return program;
    }

    public String getLogInfo(IntBuffer intBuffer, int shaderProgram) {
        if (intBuffer.get(0) != 1) {
            gl.glGetProgramiv(shaderProgram, GL4bc.GL_INFO_LOG_LENGTH, intBuffer);
            int size = intBuffer.get(0);
            if (size > 0) {
                ByteBuffer byteBuffer = ByteBuffer.allocate(size);
                gl.glGetProgramInfoLog(shaderProgram, size, intBuffer, byteBuffer);
                return new String(byteBuffer.array());
            } else {
                return "Unknown";
            }
        }
        return null;
    }

    public int createShader(String name, ShaderType type) {
        int shader = gl.glCreateShader(type.glType);
        String[] code = new String[]{readShaderProgram(name, type)};
        gl.glShaderSource(shader, 1, code, null, 0);
        gl.glCompileShader(shader);

        LOG.info("Compiled shader {}.{}", type.glType, name);
        return shader;
    }

    private void checkItem(int shader, String prefix) {
        IntBuffer intBuffer = IntBuffer.allocate(1);
        gl.glGetProgramiv(shader, GL4bc.GL_LINK_STATUS, intBuffer);
        String msg = getLogInfo(intBuffer, shader);
        if (msg != null) {
            throw new RuntimeException(prefix + ": " + msg);
        }
    }

    public String readShaderProgram(String name, ShaderType type) {
        String filename = "glsl/" + name + "." + type.extension;
        LOG.info("Reading " + filename);
        URL systemResource = ClassLoader.getSystemResource(filename);
        try {
            if (systemResource == null) {
                throw new FileNotFoundException(filename);
            }
            return IOUtils.toString(systemResource.openStream());
        } catch (IOException e) {
            throw new RuntimeException("Unable to read programm " + name, e);
        }
    }
}
