package com.MobiSeeker.PrescriptionWatcher.connection;

import android.content.Context;
import android.os.PowerManager;

import com.samsung.chord.ChordManager;
import com.samsung.chord.IChordChannel;
import com.samsung.chord.IChordChannelListener;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowPowerManager;

import java.util.ArrayList;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class GivenANodeManager {
    NodeManager nodeManager;

    @Mock
    ChordManager chordManager;

    @Mock
    IChordChannelListener chordChannelListener;

    @Mock
    ChannelInformation channelInformation;

    @Mock
    IChordChannel chordChannel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

       // this.context = Robolectric.getShadowApplication().getApplicationContext();

        this.nodeManager = new NodeManager(this.chordManager, this.chordChannelListener, this.channelInformation);
    }
    
    @Test
    public void whenConstructingShouldNotThrow() {
        Assert.assertNotNull(this.nodeManager);
    }

    @Test
    public void whenCallingGetJoinedNodeListAndChordManagerIsNoNullShouldReturnNull() {
        this.nodeManager = new NodeManager(null, null, null);

        List<String> nodes = this.nodeManager.getJoinedNodeList("CHANNELNAME");

        Assert.assertNull(nodes);
    }

    @Test
    public void whenCallingGetJoinedNodeListAndThereIsNoChannelShouldReturnNull() {
        doReturn(null).when(this.chordManager).getJoinedChannel("CHANNELNAME");

        List<String> nodes = this.nodeManager.getJoinedNodeList("CHANNELNAME");

        Assert.assertNull(nodes);
    }

    @Test
    public void whenCallingGetJoinedNodeListShouldReturnExpected() {
        doReturn(this.chordChannel).when(this.chordManager).getJoinedChannel("CHANNELNAME");

        List<String> nodes = new ArrayList<String>(1);
        doReturn(nodes).when(this.chordChannel).getJoinedNodeList();

        List<String> actual = this.nodeManager.getJoinedNodeList("CHANNELNAME");

        Assert.assertEquals(nodes, actual);
    }

    @Test
    public void whenCallingSetNodeKeepAliveTimeoutAndChordManagerIsNullShouldNotThrow() {
        this.nodeManager = new NodeManager(null, null, null);

        this.nodeManager.setNodeKeepAliveTimeout(100);
    }

    @Test
    public void whenCallingSetNodeKeepAliveTimeoutShouldNotThrow() {
        this.nodeManager.setNodeKeepAliveTimeout(100);

        verify(this.chordManager).setNodeKeepAliveTimeout(100);
    }

    @Test
    public void whenCallingGetNodeIpAddressAndChordManagerIsNoNullShouldReturnNull() {
        this.nodeManager = new NodeManager(null, null, null);

        String ipAddress = this.nodeManager.getNodeIpAddress("CHANNELNAME", "NODENAME");

        Assert.assertNull(ipAddress);
    }

    @Test
    public void whenCallingGetNodeIpAddressAndThereIsNoChannelShouldReturnNull() {
        doReturn(null).when(this.chordManager).getJoinedChannel("CHANNELNAME");

        String ipAddress = this.nodeManager.getNodeIpAddress("CHANNELNAME", "NODENAME");

        Assert.assertNull(ipAddress);
    }

    @Test
    public void whenCallingGetNodeIpAddressShouldReturnExpected() {
        doReturn(this.chordChannel).when(this.chordManager).getJoinedChannel("CHANNELNAME");

        doReturn("IPADDRESS").when(this.chordChannel).getNodeIpAddress("NODENAME");

        String ipAddress = this.nodeManager.getNodeIpAddress("CHANNELNAME",  "NODENAME");

        Assert.assertEquals("IPADDRESS", ipAddress);
    }

    @Test
    public void whenCallingGetJoinedChannelListAndChordManagerIsNoNullShouldReturnNull() {
        this.nodeManager = new NodeManager(null, null, null);

        List<IChordChannel> actual = this.nodeManager.getJoinedChannelList();

        Assert.assertNull(actual);
    }

    @Test
    public void whenCallingGetJoinedChannelListShouldReturnExpected() {
        List<IChordChannel> expected = new ArrayList<IChordChannel>(1);
        doReturn(expected).when(this.chordManager).getJoinedChannelList();

        List<IChordChannel> actual = this.nodeManager.getJoinedChannelList();

        Assert.assertEquals(expected, actual);
    }

}
