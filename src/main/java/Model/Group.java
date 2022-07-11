package Model;

import Client.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Group implements Serializable {
    private String groupName;
    private ArrayList<Role> roles = new ArrayList<Role>();
    private HashMap<Client, ArrayList<Role>> members = new HashMap<Client, ArrayList<Role>>();
    private ArrayList<Channel> channels = new ArrayList<Channel>();
    private Role creator = new Role("creator");
    private final Role member = new Role("member");

    public Group(String groupName, Client creatorOfGroup) {
        for (Ability ability : Ability.values()) {
            creator.addAbility(ability);
        }
        roles.add(creator);
        roles.add(member);
        this.groupName = groupName;
        members.put(creatorOfGroup, new ArrayList<>());
        members.get(creatorOfGroup).add(creator);
        member.addAbility(Ability.AddUser);
    }

    public String getGroupName() {
        return groupName;
    }

    public HashMap<Client, ArrayList<Role>> getMembers() {
        return members;
    }

    public ArrayList<Channel> getChannels() {
        return channels;
    }

    /**
     * Adds a new member to the group
     * @param clientCheck the client that is being added to the group
     * @return true if the client was added to the group, false if the client was already in the group
     */
    public boolean addMember(Client clientCheck) {
        for (Client client : members.keySet()) {
            if (client.getUsername().equals(clientCheck.getUsername())) {
                return false;
            }
        }
        members.put(clientCheck, new ArrayList<>());
        members.get(clientCheck).add(member);
        return true;
    }

    /**
     * create a new channel in the group
     * @param channelName the name of the channel
     * @param channelType the type of the channel
     */
    public void createChannel(String channelName, String channelType) {
        if (channelType.equals("text")) {
            channels.add(new TextChannel(channelName));
        } else if (channelType.equals("voice")) {
            channels.add(new VoiceChannel(channelName));
        }
    }

    /**
     * delete a channel from the group
     * @param channelName the name of the channel
     */
    public void deleteChannel(String channelName) {
        //remove channel from group with iterator
        channels.removeIf(channel -> channel.getName().equals(channelName));

    }

    /**
     * add a role to the group
     * @param name the name of the role
     */
    public void createRole(String name) {
        Role role = new Role(name);
        roles.add(role);
    }

    /**
     * delete a role from the group
     * @param name the name of the role
     */
    public void deleteRole(String name) {
        roles.removeIf(role -> role.getName().equals(name));
    }

    /**
     * add Abbility to a role
     * @param role the role that the ability is being added to
     * @param ability the ability that is being added to the role
     * @return the ability was added to the role or not
     */
    public boolean addAbilityToRole(String role,String ability) {
        Role addToRole = null;
        for(Role roleCheck : roles){
            if(roleCheck.getName().equals(role)){
                addToRole = roleCheck;
                break;
            }
        }
        for (Ability abilityCheck : Ability.values()) {
            if (abilityCheck.toString().equals(ability)) {
                assert addToRole != null;
                addToRole.addAbility(abilityCheck);
                return true;
            }
        }
        return false;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public ArrayList<Role> getRoles() {
        return roles;
    }
}
