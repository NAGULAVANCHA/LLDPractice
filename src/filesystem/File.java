package filesystem;

public class File extends FileSystemEntry {
    private String content;

    public File(String name) {
        super(name);
        this.content = "";
    }

    public File(String name, String content) {
        super(name);
        this.content = content;
    }

    public String getContent() { return content; }

    public void write(String content) {
        this.content = content;
        touch();
    }

    public void append(String content) {
        this.content += content;
        touch();
    }

    @Override
    public long getSize() { return content.length(); }

    @Override
    public boolean isDirectory() { return false; }

    @Override
    public void display(String indent) {
        System.out.println(indent + "📄 " + getName() + " (" + getSize() + " bytes)");
    }
}

