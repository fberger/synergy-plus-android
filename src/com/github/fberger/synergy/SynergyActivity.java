package com.github.fberger.synergy;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

public class SynergyActivity extends Activity {
	
	public static final String TAG = "Synergy";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        new Server().execute();
    }
    
    private class Server extends AsyncTask<Void, Integer, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			ChannelFactory factory =
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool());
			
			ServerBootstrap bootstrap = new ServerBootstrap(factory);
			
			bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
				public ChannelPipeline getPipeline() {
					return Channels.pipeline(new SynergyMessageDecoder(),
							new SynergyServerHandler());
				}
			});
			
			bootstrap.setOption("child.tcpNoDelay", true);
			bootstrap.setOption("child.keepAlive", true);
			
			bootstrap.bind(new InetSocketAddress(8080));
			return null;
		}
    	
    }
}