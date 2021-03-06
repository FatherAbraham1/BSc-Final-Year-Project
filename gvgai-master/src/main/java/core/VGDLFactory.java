package core;

import java.awt.Dimension;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;

import core.content.Content;
import core.content.GameContent;
import core.content.InteractionContent;
import core.content.SpriteContent;
import core.content.TerminationContent;
import core.game.BasicGame;
import core.game.Game;
import core.termination.MultiSpriteCounter;
import core.termination.SpriteCounter;
import core.termination.Termination;
import core.termination.Timeout;
import ontology.Types;
import ontology.avatar.FlakAvatar;
import ontology.avatar.HorizontalAvatar;
import ontology.avatar.MovingAvatar;
import ontology.avatar.oriented.MissileAvatar;
import ontology.avatar.oriented.OngoingAvatar;
import ontology.avatar.oriented.OrientedAvatar;
import ontology.avatar.oriented.ShootAvatar;
import ontology.effects.Effect;
import ontology.effects.binary.AttractGaze;
import ontology.effects.binary.BounceForward;
import ontology.effects.binary.CollectResource;
import ontology.effects.binary.KillIfFromAbove;
import ontology.effects.binary.KillIfOtherHasMore;
import ontology.effects.binary.PullWithIt;
import ontology.effects.binary.TeleportToExit;
import ontology.effects.binary.TransformToSingleton;
import ontology.effects.binary.WallStop;
import ontology.effects.unary.ChangeResource;
import ontology.effects.unary.CloneSprite;
import ontology.effects.unary.FlipDirection;
import ontology.effects.unary.KillIfHasLess;
import ontology.effects.unary.KillIfHasMore;
import ontology.effects.unary.KillSprite;
import ontology.effects.unary.ReverseDirection;
import ontology.effects.unary.SpawnIfHasLess;
import ontology.effects.unary.SpawnIfHasMore;
import ontology.effects.unary.StepBack;
import ontology.effects.unary.TransformTo;
import ontology.effects.unary.TurnAround;
import ontology.effects.unary.UndoAll;
import ontology.effects.unary.WrapAround;
import ontology.sprites.Conveyor;
import ontology.sprites.Door;
import ontology.sprites.Flicker;
import ontology.sprites.Immovable;
import ontology.sprites.OrientedFlicker;
import ontology.sprites.Passive;
import ontology.sprites.Resource;
import ontology.sprites.ResourcePack;
import ontology.sprites.Spreader;
import ontology.sprites.missile.ErraticMissile;
import ontology.sprites.missile.Missile;
import ontology.sprites.missile.RandomMissile;
import ontology.sprites.missile.Walker;
import ontology.sprites.missile.WalkerJumper;
import ontology.sprites.npc.AlternateChaser;
import ontology.sprites.npc.Chaser;
import ontology.sprites.npc.Fleeing;
import ontology.sprites.npc.RandomAltChaser;
import ontology.sprites.npc.RandomInertial;
import ontology.sprites.npc.RandomNPC;
import ontology.sprites.producer.Bomber;
import ontology.sprites.producer.Portal;
import ontology.sprites.producer.RandomBomber;
import ontology.sprites.producer.SpawnPoint;
import ontology.sprites.producer.SpriteProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.Vector2d;

/**
 * Created with IntelliJ IDEA.
 * User: Diego
 * Date: 22/10/13
 * Time: 15:33
 * This is a Java port from Tom Schaul's VGDL - https://github.com/schaul/py-vgdl
 */
public class VGDLFactory implements Serializable, Cloneable
{

    /**
     * Available sprites for VGDL.
     */
    private String[] spriteStrings = new String[]
            {"Conveyor", "Flicker", "Immovable", "OrientedFlicker", "Passive", "Resource", "Spreader",
             "ErraticMissile", "Missile", "RandomMissile", "Walker", "WalkerJumper",
             "ResourcePack", "Chaser", "Fleeing", "RandomInertial", "RandomNPC", "AlternateChaser", "RandomAltChaser",
             "Bomber", "RandomBomber", "Portal", "SpawnPoint", "SpriteProducer", "Door",
             "FlakAvatar", "HorizontalAvatar","MovingAvatar","MissileAvatar",
             "OrientedAvatar","ShootAvatar", "OngoingAvatar"};


    /**
     * Available Sprite classes for VGDL.
     */
    private Class[] spriteClasses = new Class[]
            {Conveyor.class, Flicker.class, Immovable.class, OrientedFlicker.class, Passive.class, Resource.class, Spreader.class,
             ErraticMissile.class, Missile.class, RandomMissile.class, Walker.class, WalkerJumper.class,
             ResourcePack.class, Chaser.class, Fleeing.class, RandomInertial.class, RandomNPC.class, AlternateChaser.class, RandomAltChaser.class,
             Bomber.class, RandomBomber.class, Portal.class, SpawnPoint.class, SpriteProducer.class, Door.class,
             FlakAvatar.class, HorizontalAvatar.class,MovingAvatar.class,MissileAvatar.class,
             OrientedAvatar.class,ShootAvatar.class, OngoingAvatar.class};

    /**
     * Available effects for VGDL.
     */
    private String[] effectStrings = new String[]
            {
                "stepBack", "turnAround", "killSprite", "transformTo", "transformToSingleton",
                    "wrapAround", "changeResource", "killIfHasLess", "killIfHasMore", "cloneSprite",
                    "flipDirection", "reverseDirection", "undoAll", "spawnIfHasMore", "spawnIfHasLess",
                "pullWithIt", "wallStop", "collectResource", "killIfOtherHasMore", "killIfFromAbove",
                "teleportToExit", "bounceForward", "attractGaze"
            };

    /**
     * Available effect classes for VGDL.
     */
    private Class[] effectClasses = new Class[]
            {
                StepBack.class, TurnAround.class, KillSprite.class, TransformTo.class, TransformToSingleton.class,
                    WrapAround.class,ChangeResource.class, KillIfHasLess.class, KillIfHasMore.class, CloneSprite.class,
                    FlipDirection.class, ReverseDirection.class, UndoAll.class, SpawnIfHasMore.class, SpawnIfHasLess.class,
                PullWithIt.class, WallStop.class, CollectResource.class, KillIfOtherHasMore.class, KillIfFromAbove.class,
                TeleportToExit.class, BounceForward.class, AttractGaze.class
            };


    /**
     * Available terminations for VGDL.
     */
    private String[] terminationStrings = new String[]
            {
                    "MultiSpriteCounter", "SpriteCounter", "Timeout"
            };

    /**
     * Available termination classes for VGDL.
     */
    private Class[] terminationClasses = new Class[]
            {
                    MultiSpriteCounter.class, SpriteCounter.class, Timeout.class
            };


    /**
     * Singleton reference to game/sprite factory
     */
    private static VGDLFactory factory;

    /**
     * Cache for registered games.
     */
    public HashMap<String, Class> registeredGames;

    /**
     * Cache for registered sprites.
     */
    public HashMap<String, Class> registeredSprites;

    /**
     * Cache for registered effects.
     */
    public HashMap<String, Class> registeredEffects;

    /**
     * Cache for registered effects.
     */
    public HashMap<String, Class> registeredTerminations;

    /**
     * Default private constructor of this singleton.
     */
    public VGDLFactory(){
        init();
    }

    private static final Logger LOG = LoggerFactory.getLogger(VGDLFactory.class);

    /**
     * Initializes the maps for caching classes.
     */
    public void init()
    {
        registeredGames = new HashMap<String, Class>();
        registeredGames.put("BasicGame", BasicGame.class);


        registeredSprites = new HashMap<String, Class>();
        for(int i = 0;  i < spriteStrings.length; ++i)
        {
            registeredSprites.put(spriteStrings[i], spriteClasses[i]);
        }

        registeredEffects  = new HashMap<String, Class>();
        for(int i = 0;  i < effectStrings.length; ++i)
        {
            registeredEffects.put(effectStrings[i], effectClasses[i]);
        }

        registeredTerminations = new HashMap<String, Class>();
        for(int i = 0;  i < terminationStrings.length; ++i)
        {
            registeredTerminations.put(terminationStrings[i], terminationClasses[i]);
        }
    }

    /**
     * Returns the unique instance of this class.
     * @return the factory that creates the game and the sprite objects.
     */
    public static VGDLFactory GetInstance()
    {
        if(factory == null)
            factory = new VGDLFactory();
        return factory;
    }

    public static VGDLFactory getNewCopy() {
        return new VGDLFactory();
    }


    /**
     * Creates a game, receiving a GameContent object
     * @param content potential parameters for the class.
     * @return The game just created.
     */
    public Game createGame(GameContent content)
    {
        try{
            Class gameClass = registeredGames.get(content.referenceClass);
            Constructor gameConstructor = gameClass.getConstructor(new Class[] {GameContent.class});
            return (Game) gameConstructor.newInstance(new Object[]{content});

        }catch (NoSuchMethodException e)
        {
            e.printStackTrace();
            LOG.info("Error creating game of class " + content.referenceClass);
        }catch (Exception e)
        {
            e.printStackTrace();
            LOG.info("Error creating game of class " + content.referenceClass);
        }

        return null;
    }

    /**
     * Creates a new sprite with a given dimension in a certain position. Parameters are passed as SpriteContent.
     * @param content parameters for the sprite, including its class.
     * @param position position of the object.
     * @param dim dimensions of the sprite on the world.
     * @return the new sprite, created and initialized, ready for play!
     */
    public VGDLSprite createSprite(SpriteContent content, Vector2d position, Dimension dim)
    {
        if (registeredSprites == null) {
            LOG.info("registeredSprites is null");
        } else if (registeredSprites.size() == 0) {
            LOG.info("registredSprites is empty");
        }
        try{
            Class spriteClass = registeredSprites.get(content.referenceClass);
            Constructor spriteConstructor = spriteClass.getConstructor
                    (new Class[] {Vector2d.class, Dimension.class, SpriteContent.class});
            return (VGDLSprite) spriteConstructor.newInstance(new Object[]{position, dim, content});

        }catch (NoSuchMethodException e)
        {
            LOG.info("Error :" + Arrays.toString(e.getStackTrace()));
            LOG.info("Error creating sprite " + content.identifier + " of class " + content.referenceClass);
        }catch (Exception e)
        {
            LOG.info("Error :" + Arrays.toString(e.getStackTrace()));
            LOG.info("Error creating sprite " + content.identifier + " of class " + content.referenceClass);
        }

        return null;
    }


    /**
     * Creates a new effect, with parameters passed as InteractionContent.
     * @param content parameters for the effect, including its class.
     * @return the new effect, created and initialized, ready to be triggered!
     */
    public Effect createEffect(InteractionContent content)
    {
        try{
            Class effectClass = registeredEffects.get(content.function);
            Constructor effectConstructor = effectClass.getConstructor
                    (new Class[] {InteractionContent.class});
            Effect ef = (Effect) effectConstructor.newInstance(new Object[]{content});
            return ef;

        }catch (NoSuchMethodException e)
        {
            e.printStackTrace();
            System.out.println("Error creating effect " + content.function + " between "
                    + content.object1 + " and " + content.object2);
        }catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Error creating effect " + content.function + " between "
                    + content.object1 + " and " + content.object2);
        }

        return null;
    }


    /**
     * Creates a new termination, with parameters passed as TerminationContent.
     * @param content parameters for the termination condition, including its class.
     * @return the new termination, created and initialized, ready to be checked!
     */
    public Termination createTermination(TerminationContent content)
    {
        try{
            Class terminationClass = registeredTerminations.get(content.identifier);
            Constructor terminationConstructor = terminationClass.getConstructor
                    (new Class[] {TerminationContent.class});
            Termination ter = (Termination) terminationConstructor.newInstance(new Object[]{content});
            return ter;

        }catch (NoSuchMethodException e)
        {
            e.printStackTrace();
            System.out.println("Error creating termination condition " + content.identifier);
        }catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Error creating termination condition " + content.identifier);
        }

        return null;
    }

    /**
     * Parses the parameters from content, assigns them to variables in obj.
     * @param content contains the parameters to read.
     * @param obj object with the variables to assign.
     */
    public void parseParameters(Content content, Object obj)
    {
        //Get all fields from the class and store it as key->field
        Field[] fields = obj.getClass().getFields();
        HashMap<String, Field> fieldMap = new HashMap<String, Field>();
        for (Field field : fields)
        {
            String strField = field.toString();
            int lastDot = strField.lastIndexOf(".");
            String fieldName = strField.substring(lastDot + 1).trim();

            fieldMap.put(fieldName, field);
        }
        Object objVal = null;
        Field cfield = null;
        //Check all parameters from content
        for (String parameter : content.parameters.keySet())
        {
            String value = content.parameters.get(parameter);
            if (fieldMap.containsKey(parameter))
            {

                try {
                    cfield = Types.class.getField(value);
                    objVal = cfield.get(null);
                } catch (Exception e) {
                    try {
                        objVal = Integer.parseInt(value);

                    } catch (NumberFormatException e1) {
                        try {
                            objVal = Double.parseDouble(value);
                        } catch (NumberFormatException e2) {
                            try {
                                if(value.equalsIgnoreCase("true") ||
                                   value.equalsIgnoreCase("false")  )
                                    objVal = Boolean.parseBoolean(value);
                                else
                                    objVal = value;
                            } catch (NumberFormatException e3) {
                                objVal = value;
                            }
                        }
                    }
                }
                try {
                    fieldMap.get(parameter).set(obj, objVal);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                System.out.println("Unknown field (" + parameter + "=" + value +
                        ") from " + content.toString());
            }
        }

    }

    /**
     * Returns the value of an int field in the object specified
     * @param obj object that holds the field.
     * @param fieldName name of the field to retrieve.
     * @return the value, or -1 if the parameter does not exist or it is not an int.
     */
    public int requestFieldValueInt(Object obj, String fieldName)
    {
        //Get all fields from the class and store it as key->field
        Field[] fields = obj.getClass().getFields();
        for (Field field : fields)
        {
            String strField = field.getName();
            if(strField.equalsIgnoreCase(fieldName))
            {
                try{
                    Object objVal = field.get(obj);
                    return ((Integer)objVal).intValue();
                }catch(Exception e)
                {
                    System.out.println("ERROR: invalid requested int parameter " + fieldName);
                    return -1;
                }
            }
        }
        return -1;
    }

}
